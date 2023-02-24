package it.ohalee.basementlib.api.bukkit.scoreboard.board;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLineImpl;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.RepeatingTask;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines.ScoreboardLineTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardImpl implements Scoreboard {

    private final String title;
    private final Set<ScoreboardLine> lines = ConcurrentHashMap.newKeySet();
    private final Player viewer;
    private ScoreboardAdapter adapter;
    private RepeatingTask updateTask;
    private boolean ready = false;

    public ScoreboardImpl(ScoreboardAdapter adapter, String title, Player viewer, @Nullable RepeatingTask updateTask) {
        this.adapter = adapter;
        this.title = title;
        this.viewer = viewer;
        this.updateTask = updateTask;
    }

    public ScoreboardImpl(ScoreboardAdapter adapter, String title, Player viewer) {
        this.adapter = adapter;
        this.title = title;
        this.viewer = viewer;
        this.updateTask = null;
    }

    @Override
    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(ScoreboardAdapter module) {
        this.adapter = module;
    }

    @Override
    public void create() {
        adapter.create(title, viewer);
        lines.forEach(ScoreboardLine::show);
        if (updateTask != null) updateTask.start();
        ready = true;
    }

    @Override
    public void destroy() {
        adapter.destroy(viewer);
        lines.forEach(ScoreboardLine::destroy);
        if (updateTask != null) updateTask.cancel();
        ready = false;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Set<ScoreboardLine> getLines() {
        return lines;
    }

    @Override
    public Player getViewer() {
        return viewer;
    }

    @Override
    public RepeatingTask getUpdateTask() {
        return updateTask;
    }

    @Override
    public void setUpdateTask(RepeatingTask updateTask) {
        this.updateTask = updateTask;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public Optional<ScoreboardLine> getLine(int row) {
        return lines.parallelStream().filter(line -> line.getRow() == row).findAny();
    }

    @Override
    public void addLine(ScoreboardLine line) {
        int row = line.getRow();
        Optional<ScoreboardLine> existing = getLine(row);
        if (existing.isPresent()) {
            setLine(existing.get(), line.getContent());
            return;
        }

        lines.add(line);
        line.show();
    }

    @Override
    public void setLine(int row, String content) {
        ScoreboardLine line = new ScoreboardLineImpl(adapter, viewer, row, content);
        addLine(line);
    }

    @Override
    public void setLine(ScoreboardLine line, String content) {
        line.setContent(content);
        line.update();
    }

    @Override
    public void setLineTask(int row, ScoreboardLineTask task) {
        getLine(row).ifPresent(line -> setLineTask(line, task));
    }

    @Override
    public void setLineTask(ScoreboardLine line, ScoreboardLineTask task) {
        if (line.getTask() != null && line.getTask().isRunning())
            line.getTask().cancel();

        line.setTask(task);
        task.start();
    }

    @Override
    public void removeLine(int row) {
        getLine(row).ifPresent(line -> {
            line.destroy();
            lines.remove(line);
        });
    }

    @Override
    public void removeLines() {
        lines.forEach(ScoreboardLine::destroy);
        lines.clear();
    }
}
