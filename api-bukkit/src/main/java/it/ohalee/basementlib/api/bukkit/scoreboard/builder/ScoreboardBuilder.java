package it.ohalee.basementlib.api.bukkit.scoreboard.builder;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.ScoreboardImpl;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLineImpl;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines.ScoreboardLineTaskImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ScoreboardBuilder<T extends Scoreboard> extends AbstractScoreboardBuilder<Scoreboard> {

    private final Class<T> type;
    private final String title;
    private final Player viewer;
    private final List<ScoreboardLine> lines = new ArrayList<>();
    private ScoreboardAdapter adapter;
    private int index = 0;

    protected ScoreboardBuilder(ScoreboardAdapter adapter, Class<T> type, String title, Player viewer) {
        this.adapter = adapter;
        this.type = type;
        this.title = title;
        this.viewer = viewer;
    }

    public static DefaultBuilder builder(ScoreboardAdapter scoreboardAdapter, String title, Player viewer) {
        return new DefaultBuilder(scoreboardAdapter, title, viewer);
    }

    @Override
    public AbstractScoreboardBuilder<Scoreboard> addLine(String content) {
        for (ScoreboardLine line : lines) {
            if (line.getRow() == index) {
                index++;
                addLine(content);
            }
        }
        return setLine(index++, content);
    }

    @Override
    public ScoreboardBuilder<T> setLine(int row, String content) {
        return setLine(row, content, -1, null);
    }

    @Override
    public ScoreboardBuilder<T> setLine(int row, String content, long period, @Nullable Consumer<ScoreboardLine> task) {
        ScoreboardLine line = new ScoreboardLineImpl(adapter, viewer, row, content, task != null ? new ScoreboardLineTaskImpl(adapter.getJavaPlugin(), period, task::accept) : null);
        return addLine(line);
    }

    @Override
    public ScoreboardBuilder<T> addLine(ScoreboardLine line) {
        lines.add(line);
        return this;
    }

    @Override
    public ScoreboardBuilder<T> addLines(Collection<ScoreboardLine> lines) {
        for (ScoreboardLine line : lines)
            addLine(line);
        return this;
    }

    @Override
    public ScoreboardBuilder<T> setAdapter(ScoreboardAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    @Override
    public T build() {
        T board;
        try {
            board = type.getConstructor(ScoreboardAdapter.class, String.class, Player.class).newInstance(adapter, title, viewer);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Something went wrong during build of the view", e);
        }

        board.setAdapter(adapter);
        for (ScoreboardLine line : lines)
            board.getLines().add(line);

        return board;
    }

    public static class DefaultBuilder extends ScoreboardBuilder<ScoreboardImpl> {
        public DefaultBuilder(ScoreboardAdapter scoreboardAdapter, String title, Player viewer) {
            super(scoreboardAdapter, ScoreboardImpl.class, title, viewer);
        }
    }
}
