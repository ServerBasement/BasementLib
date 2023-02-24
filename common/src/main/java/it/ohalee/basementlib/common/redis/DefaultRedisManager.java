package it.ohalee.basementlib.common.redis;

import it.ohalee.basementlib.api.redis.RedisCredentials;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.common.config.BasementConfig;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

import java.util.HashMap;
import java.util.Map;

public class DefaultRedisManager implements RedisManager {

    private final RedissonClient redissonClient;
    private final Map<String, RTopic> topicMap;

    public DefaultRedisManager() {
        RedisCredentials credentials = new RedisCredentials(BasementConfig.REDIS_HOSTS.get());
        Config config = new Config();
        config.setUseThreadClassLoader(false);
        config.setCodec(TypedJsonJacksonCodec.INSTANCE);
        // TODO: 23/02/2023 optional cluster and modifiable threads number
        config.setNettyThreads(64);
        config.setThreads(24);
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .setScanInterval(2000)
                .setReadMode(ReadMode.MASTER_SLAVE);
        for (String host : credentials.getHosts()) {
            clusterServersConfig.addNodeAddress("redis://" + host + ":6379");
        }
        redissonClient = Redisson.create(config);
        topicMap = new HashMap<>();
    }

    @Override
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    @Override
    public <T extends BasementMessage> int registerTopicListener(String name, BasementMessageHandler<T> basementMessageHandler) {
        RTopic topic = topicMap.get(name);
        if (topic == null) {
            topic = redissonClient.getTopic(name);
            topicMap.put(name, topic);
        }
        return topic.addListener(basementMessageHandler.getCommandClass(), basementMessageHandler);
    }

    public void unregisterTopicListener(String name, Integer... listenerId) {
        RTopic topic = topicMap.get(name);
        if (topic == null) {
            topic = redissonClient.getTopic(name);
            topicMap.put(name, topic);
        }
        topic.removeListener(listenerId);
    }

    public void clearTopicListeners(String name) {
        RTopic topic = topicMap.get(name);
        if (topic == null) {
            topic = redissonClient.getTopic(name);
            topicMap.put(name, topic);
        }
        topic.removeAllListeners();
    }

    public <T extends BasementMessage> long publishMessage(T message) {
        RTopic topic = topicMap.get(message.getTopic());
        if (topic == null) {
            topic = redissonClient.getTopic(message.getTopic());
            topicMap.put(message.getTopic(), topic);
        }
        return topic.publish(message);
    }
}
