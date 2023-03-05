package it.ohalee.basementlib.bukkit;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.bukkit.chat.Colorizer;
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
import it.ohalee.basementlib.bukkit.nms.v1_19_R1.inventory.InventoryFixer;
import it.ohalee.basementlib.bukkit.placeholders.BasementPlaceholder;
import it.ohalee.basementlib.bukkit.redis.handler.ServerShutdownHandler;
import it.ohalee.basementlib.bukkit.redis.handler.VelocityNotifyHandler;
import it.ohalee.basementlib.bukkit.scoreboard.ScoreboardManager;
import it.ohalee.basementlib.common.config.ConfigKeys;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import it.ohalee.basementlib.common.plugin.logging.JavaPluginLogger;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.redisson.remote.RemoteServiceAckTimeoutException;

import java.io.File;
import java.nio.file.Path;

public class BasementBukkitPlugin extends AbstractBasementPlugin implements BasementBukkit {

    private final JavaPlugin plugin;

    private String serverID = "unknown";
    private BukkitServer server;
    private BukkitTask task;

    private ScoreboardAdapter scoreboardAdapter;
    private ScoreboardManager scoreboardManager;

    public BasementBukkitPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        super.enable();

        if (getRedisManager() == null || getRemoteCerebrumService() == null)
            serverID = configuration.get(ConfigKeys.SERVER);

        server = new BukkitServer(getServerID());
        task = new ServerInfoRunnable(ServerStatus.OPEN).runTaskTimerAsynchronously(plugin, 10L, 20L);

        if (getRemoteVelocityService() != null) {
            try {
                getRemoteVelocityService().registerServer(getServerID(), plugin.getServer().getPort());
            } catch (RemoteServiceAckTimeoutException e) {
                plugin.getLogger().severe("Velocity is offline, server not registered");
            }
        }

        String version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        ScoreboardUtils scoreboardUtils = null;

        switch (version) {
            case "v1_8_R3" -> {
                scoreboardUtils = new it.ohalee.basementlib.bukkit.nms.v1_8_R3.scoreboard.ScoreboardUtils();
                Colorizer.setAdapter(new it.ohalee.basementlib.bukkit.nms.v1_8_R3.chat.ColorizerNMS());
            }
            case "v1_19_R1" -> {
                scoreboardUtils = new it.ohalee.basementlib.bukkit.nms.v1_19_R1.scoreboard.ScoreboardUtils();
                Colorizer.setAdapter(new it.ohalee.basementlib.bukkit.nms.v1_19_R1.chat.ColorizerNMS());
                new InventoryFixer(plugin);
            }
        }

        if (getRedisManager() != null) {
            getRedisManager().registerTopicListener(VelocityNotifyMessage.TOPIC, new VelocityNotifyHandler(this));
            getRedisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(this));
        }

        this.scoreboardAdapter = ScoreboardAdapter.builder(plugin, scoreboardUtils).build();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new BasementPlaceholder(this).register();
    }

    @Override
    public void disable() {
        if (task != null) task.cancel();
        if (getRedisManager() != null) getRedisManager().publishMessage(new BukkitNotifyShutdownMessage(getServerID()));
        if (getServerManager() != null) getServerManager().removeServer(getServerID());

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
    public Path getDataDirectory() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public ConfigurationAdapter provideConfigurationAdapter(File file, boolean create) {
        return new BukkitConfigAdapter(this, resolveConfig(file, create).toFile());
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
    public ScoreboardAdapter getScoreboardAdapter() {
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
        if (getServerManager() == null) throw new IllegalStateException("Redis is not enabled");
        return getServerManager().getServer(serverID).orElseThrow();
    }

    @RequiredArgsConstructor
    private class ServerInfoRunnable extends BukkitRunnable {
        private final ServerStatus serverStatus;

        @Override
        public void run() {
            server.setWhitelist(Bukkit.hasWhitelist());
            server.setOnline(Bukkit.getServer().getOnlinePlayers().size());
            server.setMax(Bukkit.getMaxPlayers());
            server.setStatus(serverStatus);

            if (getServerManager() != null) getServerManager().addServer(getServerID(), server);
        }
    }

}
