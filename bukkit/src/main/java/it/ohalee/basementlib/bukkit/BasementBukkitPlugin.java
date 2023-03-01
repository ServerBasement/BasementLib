package it.ohalee.basementlib.bukkit;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import it.ohalee.basementlib.api.server.BukkitServer;
import it.ohalee.basementlib.api.server.ServerStatus;
import it.ohalee.basementlib.bukkit.commands.BasementBukkitCommand;
import it.ohalee.basementlib.bukkit.placeholders.BasementPlaceholder;
import it.ohalee.basementlib.bukkit.plugin.StandardBasementBukkit;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class BasementBukkitPlugin extends AbstractBasementPlugin {

    private final JavaPlugin plugin;
    private BasementBukkit basement;
    private BukkitTask task;

    private BukkitServer server;

    public BasementBukkitPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void init() {
        basement = new StandardBasementBukkit(this, plugin);
    }

    private void setupServer() {
        server = new BukkitServer(basement.getServerID());
    }

    @Override
    public void enable() {
        super.enable();
        setupServer();

        task = new ServerInfoRunnable(ServerStatus.OPEN).runTaskTimerAsynchronously(plugin, 10L, 20L);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BasementPlaceholder(basement).register();
        }
    }

    @Override
    public void disable() {
        if (task != null) task.cancel();
        getBasement().getRedisManager().publishMessage(new BukkitNotifyShutdownMessage(basement.getServerID()));
        getBasement().getServerManager().removeServer(basement.getServerID());
        super.disable();
    }

    @Override
    public File getConfig() {
        return config;
    }

    @Override
    protected void registerApiOnPlatform(BasementLib basement) {
        plugin.getServer().getServicesManager().register(BasementLib.class, basement, plugin, ServicePriority.Normal);
        plugin.getServer().getServicesManager().register(BasementBukkitPlugin.class, (BasementBukkitPlugin) basement, plugin, ServicePriority.Normal);
    }

    @Override
    protected void registerCommands() {
        plugin.getCommand("basement").setExecutor(new BasementBukkitCommand(basement));
    }

    @Override
    protected void registerListeners() {
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
            getBasement().getServerManager().addServer(basement.getServerID(), server);
        }
    }

}
