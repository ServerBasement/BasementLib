package it.ohalee.basementlib.api.bukkit.scoreboard;

import it.ohalee.basementlib.api.bukkit.scoreboard.board.Scoreboard;

import java.util.Map;
import java.util.UUID;

public interface IScoreboardManager {

    void start();

    void stop();

    Map<UUID, Scoreboard> getScoreboards();

    void forceUpdate();
}
