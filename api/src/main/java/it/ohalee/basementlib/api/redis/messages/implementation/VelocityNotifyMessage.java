package it.ohalee.basementlib.api.redis.messages.implementation;

import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import lombok.Getter;

@Getter
public class VelocityNotifyMessage extends BasementMessage {

    public static final String TOPIC = "velocity-notify";

    private final boolean shutdown;

    public VelocityNotifyMessage() {
        super(TOPIC);
        this.shutdown = false;
    }

    public VelocityNotifyMessage(boolean shutdown) {
        super(TOPIC);
        this.shutdown = shutdown;
    }
}
