package it.ohalee.basementlib.bukkit.scoreboard;

import it.ohalee.basementlib.api.bukkit.scoreboard.IScoreboardManager;
import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardProvider;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager implements Listener, IScoreboardManager {

    private final JavaPlugin plugin;
    private final ScoreboardProvider provider;
    private final int delay;

    private final Map<UUID, Scoreboard> scoreboars;
    private final BukkitRunnable taskRunnable;
    private BukkitTask updateTask;

    public ScoreboardManager(JavaPlugin plugin, ScoreboardProvider provider, int delay) {
        this.plugin = plugin;
        this.provider = provider;
        this.delay = delay;

        this.scoreboars = new ConcurrentHashMap<>();

        taskRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Scoreboard scoreboard : scoreboars.values()) {
                    if (scoreboard.getViewer() == null || !scoreboard.getViewer().isOnline()) return;
                    Player player = scoreboard.getViewer();
                    List<ScoreboardLine> lines = provider.getLines(player);

                    if (scoreboard.getLines().size() != lines.size()) {
                        remove(player);
                        setup(player);
                        return;
                    }

                    for (ScoreboardLine line : lines) {
                        scoreboard.getLine(line.getRow()).ifPresent(scoreboardLine -> {
                            scoreboardLine.setContent(line.getContent());
                            scoreboardLine.update();
                        });
                    }
                }
            }
        };

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getOnlinePlayers().forEach(this::setup);
    }

    public void start() {
        this.updateTask = taskRunnable.runTaskTimerAsynchronously(plugin, 2L, delay);
    }

    private void setup(Player player) {
        Optional.ofNullable(scoreboars.remove(player.getUniqueId())).ifPresent(Scoreboard::destroy);

        scoreboars.put(player.getUniqueId(), provider.show(player));
    }

    private void remove(Player player) {
        Optional.ofNullable(scoreboars.remove(player.getUniqueId())).ifPresent(Scoreboard::destroy);
    }

    public Map<UUID, Scoreboard> getScoreboars() {
        return Collections.unmodifiableMap(scoreboars);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (event.getPlayer().isOnline()) {
                setup(event.getPlayer());
            }
        }, 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    public void stop() {
        updateTask.cancel();
        plugin.getServer().getOnlinePlayers().forEach(this::remove);
        scoreboars.clear();
    }

    public void forceUpdate() {
        taskRunnable.run();
    }
}
