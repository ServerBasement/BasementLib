package it.ohalee.basementlib.api.bukkit.scoreboard;

import it.ohalee.basementlib.api.bukkit.scoreboard.adapter.ScoreboardAdapter;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLine;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.line.ScoreboardLineImpl;
import it.ohalee.basementlib.api.bukkit.scoreboard.board.task.ScoreboardUpdateTask;
import it.ohalee.basementlib.api.bukkit.scoreboard.builder.ScoreboardBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public interface ScoreboardProvider {

    String getTitle(Player player);

    List<ScoreboardLine> getLines(Player player);

    JavaPlugin getPlugin();

    default long getUpdatePeriod() {
        return 0L;
    }

    ScoreboardAdapter getAdapter();

    default List<ScoreboardLine> build(Player player, List<String> lines) {
        int i = 0;

        List<ScoreboardLine> scoreboardLines = new ArrayList<>();

        for (String line : lines) {
            ScoreboardLine scoreboardLine = new ScoreboardLineImpl(getAdapter(), player, i, line);
            scoreboardLines.add(scoreboardLine);
            i++;
        }

        return scoreboardLines;
    }

    default Scoreboard getBoard(Player viewer) {
        return ScoreboardBuilder.builder(getAdapter(), getTitle(viewer), viewer)
                .addLines(getLines(viewer))
                .build();
    }

    default Scoreboard show(Player viewer) {
        Scoreboard board = getBoard(viewer);
        if (getUpdatePeriod() > 0) {
            ScoreboardUpdateTask task = new ScoreboardUpdateTask(getPlugin(), getUpdatePeriod(), board, this);
            board.setUpdateTask(task);
        }

        board.create();

        return board;
    }
}
