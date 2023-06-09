package it.ohalee.basementlib.api.server;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class BukkitServer implements Comparable<BukkitServer> {

    private final String name;
    private Set<String> players;
    private int max;
    private boolean whitelist;
    private ServerStatus status;

    private BukkitServer() {
        this.name = null;
    }

    public BukkitServer(String name, Set<String> players, int max, boolean whitelist, ServerStatus status) {
        this.name = name;
        this.players = players;
        this.max = max;
        this.whitelist = whitelist;
        this.status = status;
    }

    public int getOnline() {
        return players.size();
    }

    public boolean isServerOnline() {
        return status == ServerStatus.OPEN;
    }

    public boolean isServerFull() {
        return players.size() >= max;
    }

    @Override
    public int compareTo(BukkitServer o) {
        return name.compareTo(o.getName());
    }
}
