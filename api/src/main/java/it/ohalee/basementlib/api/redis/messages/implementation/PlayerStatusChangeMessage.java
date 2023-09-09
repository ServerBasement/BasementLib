package it.ohalee.basementlib.api.redis.messages.implementation;

import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import lombok.Getter;

@Getter
public class PlayerStatusChangeMessage extends BasementMessage {

    public static final String TOPIC = "velocity-player-status-change";

    private final String username;
    private final String uuid;
    private final boolean online;

    public PlayerStatusChangeMessage() {
        super(TOPIC);
        this.username = null;
        this.uuid = null;
        this.online = false;
    }

    public PlayerStatusChangeMessage(String username, String uuid, boolean online) {
        super(TOPIC);
        this.username = username;
        this.uuid = uuid;
        this.online = online;
    }
}
