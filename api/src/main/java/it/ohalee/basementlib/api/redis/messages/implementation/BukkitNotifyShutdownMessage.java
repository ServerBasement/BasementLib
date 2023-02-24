package it.ohalee.basementlib.api.redis.messages.implementation;

import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import lombok.Getter;

@Getter
public class BukkitNotifyShutdownMessage extends BasementMessage {

    public static final String TOPIC = "bukkit-shutdown-notify";

    private final String serverId;

    public BukkitNotifyShutdownMessage() {
        super(TOPIC);
        this.serverId = null;
    }

    public BukkitNotifyShutdownMessage(String serverId) {
        super(TOPIC);
        this.serverId = serverId;
    }
}
