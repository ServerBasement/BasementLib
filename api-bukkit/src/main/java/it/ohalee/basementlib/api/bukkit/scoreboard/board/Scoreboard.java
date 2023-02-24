package it.ohalee.basementlib.api.bukkit.scoreboard.board;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.RepeatingTask;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.lines.ScoreboardLineTask;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

public interface Scoreboard {

    ScoreboardAdapter getAdapter();

    void setAdapter(ScoreboardAdapter adapter);

    void create();

    void destroy();

    String getTitle();

    Set<ScoreboardLine> getLines();

    Player getViewer();

    RepeatingTask getUpdateTask();

    void setUpdateTask(RepeatingTask task);

    boolean isReady();

    Optional<ScoreboardLine> getLine(int row);

    void addLine(ScoreboardLine line);

    void setLine(int row, String content);

    void setLine(ScoreboardLine line, String content);

    void setLineTask(int row, ScoreboardLineTask task);

    void setLineTask(ScoreboardLine line, ScoreboardLineTask task);

    void removeLine(int row);

    void removeLines();
}
