package it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines;

import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.RepeatingTask;
import org.bukkit.plugin.Plugin;

public class ScoreboardLineTaskImpl extends RepeatingTask implements ScoreboardLineTask {

    private final ScoreboardLineTrigger trigger;

    private ScoreboardLine line;

    public ScoreboardLineTaskImpl(Plugin plugin, long period, ScoreboardLineTrigger trigger) {
        super(plugin, period);
        this.trigger = trigger;
    }

    @Override
    public ScoreboardLine getLine() {
        return line;
    }

    @Override
    public void setLine(ScoreboardLine line) {
        this.line = line;
    }

    @Override
    public ScoreboardLineTrigger getTrigger() {
        return trigger;
    }

    @Override
    public void run() {
        trigger.accept(line);
    }
}
