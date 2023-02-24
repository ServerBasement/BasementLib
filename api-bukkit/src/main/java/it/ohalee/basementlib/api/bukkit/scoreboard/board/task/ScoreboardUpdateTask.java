package it.ohalee.basementlib.api.bukkit.scoreboard.board.task;

import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardProvider;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;
import org.bukkit.plugin.Plugin;

public class ScoreboardUpdateTask extends RepeatingTask {

    private final Scoreboard scoreboard;
    private final ScoreboardProvider provider;

    public ScoreboardUpdateTask(Plugin plugin, long period, Scoreboard scoreboard, ScoreboardProvider provider) {
        super(plugin, period, true);

        this.scoreboard = scoreboard;
        this.provider = provider;
    }

    @Override
    public void run() {
        scoreboard.removeLines();
        provider.getLines(scoreboard.getViewer()).forEach(scoreboard::addLine);
    }
}
