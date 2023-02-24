package it.ohalee.basementlib.bukkit.plugin;

import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.bukkit.chat.Colorizer;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardProvider;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils;
import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import it.ohalee.basementlib.api.server.BukkitServer;
import it.ohalee.basementlib.bukkit.nms.v1_19_R1.inventory.InventoryFixer;
import it.ohalee.basementlib.bukkit.redis.handler.ServerShutdownHandler;
import it.ohalee.basementlib.bukkit.redis.handler.VelocityNotifyHandler;
import it.ohalee.basementlib.bukkit.scoreboard.ScoreboardManager;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import it.ohalee.basementlib.common.plugin.StandardBasement;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.remote.RemoteServiceAckTimeoutException;

public class StandardBasementBukkit extends StandardBasement implements BasementBukkit {

    private final JavaPlugin plugin;
    private final ScoreboardAdapter scoreboardAdapter;

    private ScoreboardManager scoreboardManager;

    @Setter
    private String serverID = "unknown"; // TODO: 23/02/2023 CONFIGURABLE IN CONFIG.YML (not with cerebrum)

    public StandardBasementBukkit(AbstractBasementPlugin basementPlugin, JavaPlugin plugin) {
        super(basementPlugin);

        this.plugin = plugin;
        //setServerID(plugin.getServer().getServerName());

        try {
            getRemoteVelocityService().registerServer(getServerID(), plugin.getServer().getPort());
        } catch (RemoteServiceAckTimeoutException e) {
            plugin.getLogger().severe("Velocity is offline, server not registered");
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

        getRedisManager().registerTopicListener(VelocityNotifyMessage.TOPIC, new VelocityNotifyHandler(this));
        getRedisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(this));

        this.scoreboardAdapter = ScoreboardAdapter.builder(plugin, scoreboardUtils).build();
    }

    @Override
    public void stop() {
        super.stop();
        if (scoreboardManager != null) scoreboardManager.stop();
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
        return getServerManager().getServer(serverID).get();
    }

    @Override
    public String getServerID() {
        return serverID;
    }

}
