package it.ohalee.basementlib.api.bukkit.scoreboard.board.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public abstract class RepeatingTask extends BukkitRunnable {

    private final Plugin plugin;
    private final long period;
    private final boolean async;

    @Getter
    private int id = 0;

    protected RepeatingTask(Plugin plugin, long period) {
        this(plugin, period, true);
    }

    public void start() {
        BukkitTask task = async ? runTaskTimerAsynchronously(plugin, 0, period) : runTaskTimer(plugin, 0L, period);
        this.id = task.getTaskId();
    }

    public void cancel() {
        if (id > 0) Bukkit.getScheduler().cancelTask(id);
    }

    public boolean isRunning() {
        return Bukkit.getScheduler().isQueued(id) || Bukkit.getScheduler().isCurrentlyRunning(id);
    }
}
