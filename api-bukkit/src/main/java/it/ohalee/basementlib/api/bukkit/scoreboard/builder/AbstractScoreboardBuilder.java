package it.ohalee.basementlib.api.bukkit.scoreboard.builder;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractScoreboardBuilder<T extends Scoreboard> {

    public abstract AbstractScoreboardBuilder<T> addLine(String content);

    public AbstractScoreboardBuilder<T> setLine(int row, String content) {
        return setLine(row, content, -1, null);
    }

    public abstract AbstractScoreboardBuilder<T> setLine(int row, String content, long period, Consumer<ScoreboardLine> consumer);

    public abstract AbstractScoreboardBuilder<T> addLine(ScoreboardLine line);

    public abstract AbstractScoreboardBuilder<T> addLines(Collection<ScoreboardLine> lines);

    public abstract AbstractScoreboardBuilder<T> setAdapter(ScoreboardAdapter adapter);

    public abstract T build();
}
