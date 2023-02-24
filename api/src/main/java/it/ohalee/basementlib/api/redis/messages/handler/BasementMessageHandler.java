package it.ohalee.basementlib.api.redis.messages.handler;

import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import org.redisson.api.listener.MessageListener;

public interface BasementMessageHandler<T extends BasementMessage> extends MessageListener<T> {

    @Override
    default void onMessage(CharSequence channel, T message) {
        execute(message);
    }

    /**
     * This method will be executed when received a message of this type
     *
     * @param message the {@link BasementMessage}
     */
    void execute(T message);

    /**
     * The class of the {@link BasementMessage}
     *
     * @return the class of the {@link BasementMessage}
     */
    Class<T> getCommandClass();
}
