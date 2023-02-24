package it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines;

import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;

import java.util.function.Consumer;

@FunctionalInterface
public interface ScoreboardLineTrigger extends Consumer<ScoreboardLine> {

    /**
     * Performs the implemented action when the line updates.
     *
     * @param line the {@link ScoreboardLine}
     */
    void accept(ScoreboardLine line);
}
