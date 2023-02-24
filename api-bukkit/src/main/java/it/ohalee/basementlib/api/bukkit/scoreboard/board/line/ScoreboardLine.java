package it.ohalee.basementlib.api.bukkit.scoreboard.board.line;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines.ScoreboardLineTask;
import org.bukkit.entity.Player;

public interface ScoreboardLine {

    ScoreboardAdapter getAdapter();

    Player getViewer();

    int getRow();

    String getContent();

    void setContent(String content);

    ScoreboardLineTask getTask();

    void setTask(ScoreboardLineTask task);

    void show();

    void destroy();

    void update();

    String getTeamName();

    String getPrefix();

    String getSuffix();
}
