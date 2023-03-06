package it.ohalee.basementlib.api.redis;

import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import org.redisson.api.RedissonClient;

public interface RedisManager {

    /**
     * Gets the RedissonClient
     *
     * @return the RedissonClient instance
     */
    RedissonClient redissonClient();

    /**
     * Register a new listener for a {@link BasementMessage}
     *
     * @param name                   the topic name
     * @param basementMessageHandler the handler that will execute when message is received
     * @param <T>                    the {@link BasementMessage} whose you register the listener
     * @return the listener ID
     */
    <T extends BasementMessage> int registerTopicListener(String name, BasementMessageHandler<T> basementMessageHandler);

    /**
     * Unregister some listeners of a {@link BasementMessage}
     *
     * @param name       the topic name
     * @param listenerId the listener ID return from {@link RedisManager#registerTopicListener(String, BasementMessageHandler)}
     */
    void unregisterTopicListener(String name, Integer... listenerId);

    /**
     * Unregister all listeners of a {@link BasementMessage}
     *
     * @param name the topic name
     */
    void clearTopicListeners(String name);

    /**
     * Send a message to all listening redis client
     *
     * @param message the {@link BasementMessage}
     * @param <T>     the {@link BasementMessage}
     * @return how many clients receive the message
     */
    <T extends BasementMessage> long publishMessage(T message);
}
