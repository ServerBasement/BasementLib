package it.ohalee.basementlib.api.redis.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BasementMessage {

    private final String topic;

    protected BasementMessage() {
        this.topic = null;
    }

}
