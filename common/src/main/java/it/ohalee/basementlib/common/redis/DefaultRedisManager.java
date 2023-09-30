package it.ohalee.basementlib.common.redis;

import it.ohalee.basementlib.api.redis.RedisCredentials;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.redis.messages.BasementMessage;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SingleServerConfig;

import java.util.HashMap;
import java.util.Map;

public class DefaultRedisManager implements RedisManager {

    private final RedissonClient redissonClient;
    private final Map<String, RTopic> topicMap;

    public DefaultRedisManager(RedisCredentials credentials) {
        Config config = new Config();
        config.setUseThreadClassLoader(false);
        config.setCodec(TypedJsonJacksonCodec.INSTANCE);
        config.setNettyThreads(credentials.nettyThreads());
        config.setThreads(credentials.threads());

        if (credentials.hosts().size() > 1) {
            System.out.println("Using cluster mode... " + credentials.hosts().size() + " hosts");
            credentials.hosts().forEach(System.out::println);

            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .setScanInterval(2000)
                    .setReadMode(ReadMode.MASTER_SLAVE)
                    .setCheckSlotsCoverage(false);
            for (String host : credentials.hosts()) {
                String[] split = host.split(":");
                if (split.length == 1) clusterServersConfig.addNodeAddress("redis://" + split[0] + ":6379");
                else clusterServersConfig.addNodeAddress("redis://" + split[0] + ":" + split[1]);
            }

            if (credentials.username() != null && !credentials.username().isEmpty())
                clusterServersConfig.setUsername(credentials.username());
            if (credentials.password() != null && !credentials.password().isEmpty())
                clusterServersConfig.setPassword(credentials.password());
        } else {
            System.out.println("Using single server mode... " + credentials.hosts().size() + " host");
            credentials.hosts().forEach(System.out::println);

            SingleServerConfig serverConfig = config.useSingleServer();
            serverConfig.setDatabase(credentials.database());

            String[] split = credentials.hosts().get(0).split(":");
            if (split.length == 1) serverConfig.setAddress("redis://" + split[0] + ":6379");
            else serverConfig.setAddress("redis://" + split[0] + ":" + split[1]);

            if (credentials.username() != null && !credentials.username().isEmpty())
                serverConfig.setUsername(credentials.username());
            if (credentials.password() != null && !credentials.password().isEmpty())
                serverConfig.setPassword(credentials.password());
        }

        redissonClient = Redisson.create(config);
        topicMap = new HashMap<>();
    }

    @Override
    public RedissonClient redissonClient() {
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
