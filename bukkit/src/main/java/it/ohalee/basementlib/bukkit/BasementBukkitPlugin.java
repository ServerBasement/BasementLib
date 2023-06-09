package it.ohalee.basementlib.bukkit;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.bukkit.chat.Colorizer;
import it.ohalee.basementlib.api.bukkit.events.BasementNewServerFound;
import it.ohalee.basementlib.api.bukkit.events.BasementServerRemoved;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardProvider;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils;
import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import it.ohalee.basementlib.api.server.BukkitServer;
import it.ohalee.basementlib.api.server.ServerStatus;
import it.ohalee.basementlib.bukkit.commands.BasementBukkitCommand;
import it.ohalee.basementlib.bukkit.placeholders.BasementPlaceholder;
import it.ohalee.basementlib.bukkit.redis.handler.ServerShutdownHandler;
import it.ohalee.basementlib.bukkit.redis.handler.VelocityNotifyHandler;
import it.ohalee.basementlib.bukkit.scoreboard.ScoreboardManager;
import it.ohalee.basementlib.common.config.ConfigKeys;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import it.ohalee.basementlib.common.plugin.logging.JavaPluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.redisson.remote.RemoteServiceAckTimeoutException;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;

public class BasementBukkitPlugin extends AbstractBasementPlugin implements BasementBukkit {

    private final JavaPlugin plugin;

    private String serverID = "unknown";
    private @Nullable BukkitServer server;
    private @Nullable BukkitTask task;

    // Can be null if the version is not supported by the plugin
    private @Nullable ScoreboardAdapter scoreboardAdapter;
    private ScoreboardManager scoreboardManager;

    public BasementBukkitPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        super.enable();

        if (redisManager() == null || remoteCerebrumService() == null)
            serverID = configuration.get(ConfigKeys.SERVER);

        if (serverManager() != null) {
            server = new BukkitServer(getServerID(), Collections.emptySet(), 1, false, ServerStatus.OPEN);
            task = new ServerInfoRunnable().runTaskTimerAsynchronously(plugin, 10L, 20L);

            serverManager().setServerAddConsumer(server -> Bukkit.getPluginManager().callEvent(new BasementNewServerFound(server)));
            serverManager().setServerRemoveConsumer(server -> Bukkit.getPluginManager().callEvent(new BasementServerRemoved(server)));
        }

        if (remoteVelocityService() != null) {
            try {
                remoteVelocityService().registerServer(getServerID(), plugin.getServer().getPort());
            } catch (RemoteServiceAckTimeoutException e) {
                plugin.getLogger().severe("Velocity is offline, server not registered");
            }
        }

        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            Class<?> colorAdapter = Class.forName("it.ohalee.basementlib.bukkit.nms." + version + ".chat.ColorizerNMS");
            Colorizer.setAdapter((Colorizer.ColorAdapter) colorAdapter.newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ignored) {}

        try {
            Class<?> scoreboardAdapter = Class.forName("it.ohalee.basementlib.bukkit.nms." + version + ".scoreboard.ScoreboardUtils");
            ScoreboardUtils scoreboardUtils = (ScoreboardUtils) scoreboardAdapter.newInstance();

            this.scoreboardAdapter = ScoreboardAdapter.builder(plugin, scoreboardUtils).build();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            plugin.getLogger().warning("Scoreboard API not supported on this version of Minecraft (" + version + ")");
        }

        if (redisManager() != null) {
            redisManager().registerTopicListener(VelocityNotifyMessage.TOPIC, new VelocityNotifyHandler(this));
            redisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(this));
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new BasementPlaceholder(this).register();
    }

    @Override
    public void disable() {
        if (task != null) task.cancel();
        if (redisManager() != null) redisManager().publishMessage(new BukkitNotifyShutdownMessage(getServerID()));
        if (serverManager() != null) serverManager().removeServer(getServerID());

        super.disable();
    }

    @Override
    public PluginLogger provideLogger() {
        return new JavaPluginLogger(plugin.getLogger());
    }

    @Override
    protected void registerApiOnPlatform(BasementLib basement) {
        plugin.getServer().getServicesManager().register(BasementLib.class, basement, plugin, ServicePriority.Normal);
        plugin.getServer().getServicesManager().register(BasementBukkitPlugin.class, (BasementBukkitPlugin) basement, plugin, ServicePriority.Normal);
    }

    @Override
    protected void registerCommands() {
        plugin.getCommand("basement").setExecutor(new BasementBukkitCommand(this));
    }

    @Override
    protected void registerListeners() {
    }

    @Override
    public Path dataDirectory() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public ConfigurationAdapter provideConfigurationAdapter(Class<?> clazz, File file, boolean create) {
        return new BukkitConfigAdapter(this, resolveConfig(clazz, file, create).toFile());
    }

    @Override
    public String getServerID() {
        return serverID;
    }

    @Override
    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @Nullable ScoreboardAdapter getScoreboardAdapter() {
        return scoreboardAdapter;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public void registerScoreboard(ScoreboardProvider scoreboardProvider, int delay) {
        scoreboardManager = new ScoreboardManager(plugin, scoreboardProvider, delay);
    }

    @Override
    public BukkitServer getServer() {
        if (serverManager() == null) throw new IllegalStateException("Redis is not enabled");
        return serverManager().getServer(serverID).orElseThrow(RuntimeException::new);
    }

    private class ServerInfoRunnable extends BukkitRunnable {

        @Override
        public void run() {
            server.setWhitelist(Bukkit.hasWhitelist());
            server.setPlayers(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toSet()));
            server.setMax(Bukkit.getMaxPlayers());

            serverManager().addServer(getServerID(), server);
        }
    }

}
