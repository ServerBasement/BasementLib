package it.ohalee.basementlib.api.server;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class BukkitServer {

    private final String name;
    private int online;
    private int max;
    private boolean whitelist;
    private ServerStatus status;

    private BukkitServer() {
        this.name = null;
    }

    public BukkitServer(String name, int online, int max, boolean whitelist, ServerStatus status) {
        this.name = name;
        this.online = online;
        this.max = max;
        this.whitelist = whitelist;
        this.status = status;
    }

    public boolean isServerOnline() {
        return status == ServerStatus.OPEN;
    }

}
