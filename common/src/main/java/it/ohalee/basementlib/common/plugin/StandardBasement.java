package it.ohalee.basementlib.common.plugin;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.persistence.generic.Holder;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.plugin.BasementPlugin;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.remote.RemoteCerebrumService;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.api.server.ServerManager;
import it.ohalee.basementlib.common.config.BasementConfig;
import it.ohalee.basementlib.common.persistence.hikari.TypeHolder;
import it.ohalee.basementlib.common.persistence.maria.structure.column.connector.MariaConnector;
import it.ohalee.basementlib.common.redis.DefaultRedisManager;
import it.ohalee.basementlib.common.server.DefaultServerManager;
import org.redisson.api.RRemoteService;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class StandardBasement implements BasementLib {

    private final UUID uuid = UUID.randomUUID();

    private final RedisManager redisManager;
    private final ServerManager serverManager;
    private final RemoteVelocityService velocityService;
    private final RemoteCerebrumService cerebrumService;

    private final AbstractMariaDatabase database;

    private final HashMap<Class<?>, Holder> holderBucket = new HashMap<>();
    private final AbstractMariaHolder holder;

    public StandardBasement(BasementPlugin plugin) {
        redisManager = new DefaultRedisManager();

        Connector connector = getConnector(10, 10, "basement");
        connector.connect(BasementConfig.MARIA_HOST.get(), BasementConfig.MARIA_USERNAME.get(), BasementConfig.MARIA_PASSWORD.get());
        holder = getHolder(BasementLib.class, AbstractMariaHolder.class);
        holder.setConnector(connector);
        database = holder.createDatabase("minecraft").ifNotExists(true).build().execReturn();

        serverManager = new DefaultServerManager(this);

        RRemoteService remoteService = redisManager.getRedissonClient().getRemoteService();
        velocityService = remoteService.get(RemoteVelocityService.class);
        cerebrumService = remoteService.get(RemoteCerebrumService.class); // TODO: 23/02/2023 can be null (?)
    }

    public void start() {
    }

    public void stop() {
        holder.close();
        redisManager.getRedissonClient().shutdown();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void reloadConfig() {
        // TODO: 23/02/2023
    }

    @Override
    public RedisManager getRedisManager() {
        return redisManager;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public RemoteVelocityService getRemoteVelocityService() {
        return velocityService;
    }

    @Override
    public RemoteCerebrumService getRemoteCerebrumService() {
        return cerebrumService;
    }

    @Override
    public AbstractMariaDatabase getDatabase() {
        return database;
    }

    @Override
    public Connector getConnector(int minPoolSize, int maxPoolSize, String poolName) {
        return new MariaConnector(minPoolSize, maxPoolSize, poolName);
    }

    @Override
    public <T extends Holder> T getHolder(Class<?> key, Class<T> type) {
        Holder holder = holderBucket.get(key);
        if (holder == null) {
            try {
                T instance = type.cast(TypeHolder.TYPES.get(type).getDeclaredConstructor().newInstance());
                holderBucket.put(key, instance);
                return instance;
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return type.cast(holder);
    }

}
