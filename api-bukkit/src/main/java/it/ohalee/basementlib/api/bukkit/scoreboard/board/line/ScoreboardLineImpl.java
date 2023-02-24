package it.ohalee.basementlib.api.bukkit.scoreboard.board.line;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines.ScoreboardLineTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ScoreboardLineImpl implements ScoreboardLine {

    private final ScoreboardAdapter adapter;

    private final Player viewer;
    private final int row;
    private String oldContent = "";
    private String content;
    private ScoreboardLineTask task;

    public ScoreboardLineImpl(ScoreboardAdapter adapter, Player viewer, int row, String content, @Nullable ScoreboardLineTask task) {
        this.adapter = adapter;
        this.viewer = viewer;
        this.row = row;
        this.content = content;
        this.task = task;
    }

    public ScoreboardLineImpl(ScoreboardAdapter adapter, Player viewer, int row, String content) {
        this(adapter, viewer, row, content, null);
    }

    public ScoreboardLineImpl(ScoreboardAdapter adapter, Player viewer, int row) {
        this(adapter, viewer, row, "", null);
    }

    @Override
    public void show() {
        adapter.showLine(getViewer(), row, getTeamName(), getPrefix(), getSuffix());
        if (task != null) {
            task.setLine(this);
            if (!task.isRunning()) task.start();
        }
    }

    @Override
    public void destroy() {
        adapter.destroyLine(getViewer(), getTeamName());
        if (task != null)
            if (task.isRunning()) task.cancel();
    }

    @Override
    public void update() {
        if (oldContent.equalsIgnoreCase(content))
            return;

        adapter.updateLine(getViewer(), oldContent, content, getRow());
    }

    @Override
    public String getTeamName() {
        return "_team_" + row;
    }

    @Override
    public String getPrefix() {
        return content.substring(0, Math.min(content.length(), adapter.getCharactersLimits()));
    }

    @Override
    public String getSuffix() {
        return content.substring(Math.min(content.length(), adapter.getCharactersLimits()));
    }

    @Override
    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Player getViewer() {
        return viewer;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.oldContent = this.content;
        this.content = content;
    }

    @Override
    public ScoreboardLineTask getTask() {
        return task;
    }

    @Override
    public void setTask(ScoreboardLineTask task) {
        this.task = task;
    }
}
