package it.ohalee.basementlib.api.bukkit;

import it.ohalee.basementlib.api.bukkit.scoreboard.IScoreboardManager;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardProvider;
import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.server.BukkitServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BasementBukkit {

    /**
     * Gets the {@link JavaPlugin} instance
     *
     * @return java plugin instance
     */
    JavaPlugin getPlugin();

    /**
     * Gets the ScoreboardAdapter for managing scoreboard actions.
     * If the version is not supported, this method will return null
     * and the scoreboard API will not work
     *
     * @return the Scoreboard adapter
     */
    @Nullable ScoreboardAdapter getScoreboardAdapter();

    /**
     * Register a custom Scoreboard
     *
     * @param scoreboardProvider the class that create scoreboard
     * @param delay              time in ticks that scoreboard refresh
     */
    void registerScoreboard(ScoreboardProvider scoreboardProvider, int delay);

    /**
     * Gets the ScoreboardManager
     *
     * @return the Scoreboard manager
     */
    IScoreboardManager getScoreboardManager();

    /**
     * Gets the server object
     *
     * @return the server object
     */
    BukkitServer getServer();

    /**
     * Gets if the server ID
     *
     * @return the server ID
     */
    String getServerID();

    /**
     * Set a new server id
     *
     * @param serverID the server id
     */
    void setServerID(String serverID);

}
