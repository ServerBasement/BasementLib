package it.ohalee.basementlib.common.plugin;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.BasementProvider;
import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.generic.Holder;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.plugin.BasementPlugin;
import it.ohalee.basementlib.api.redis.RedisCredentials;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.remote.RemoteCerebrumService;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.api.server.ServerManager;
import it.ohalee.basementlib.common.config.BasementConfiguration;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.common.config.ConfigKeys;
import it.ohalee.basementlib.common.persistence.hikari.TypeHolder;
import it.ohalee.basementlib.common.persistence.maria.structure.column.connector.MariaConnector;
import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.common.redis.DefaultRedisManager;
import it.ohalee.basementlib.common.server.DefaultServerManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.redisson.api.RRemoteService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public abstract class AbstractBasementPlugin implements BasementPlugin, BasementLib {

    private final UUID uuid = UUID.randomUUID();

    private @Nullable RedisManager redisManager;
    private @Nullable ServerManager serverManager;
    private @Nullable RemoteVelocityService velocityService;
    private @Nullable RemoteCerebrumService cerebrumService;

    private @Nullable AbstractMariaDatabase database;
    private @Nullable AbstractMariaHolder holder;
    private final HashMap<Class<?>, Holder> holderBucket = new HashMap<>();

    protected ConfigurationAdapter adapter;
    protected BasementConfiguration configuration;

    private PluginLogger logger;

    public final void load() {
        getLogger().info("Loading BasementLib...");
        logger = provideLogger();
    }

    public void enable() {
        getLogger().info("Loading configuration...");
        this.adapter = provideConfigurationAdapter("config.yml", true);
        this.configuration = new BasementConfiguration(this, adapter);

        StorageCredentials storageCredentials = configuration.get(ConfigKeys.MYSQL_CREDENTIALS);
        if (storageCredentials.enabled()) {
            getLogger().info("Loading database...");
            Connector connector = createConnector(storageCredentials.minIdleConnections(), storageCredentials.maxPoolSize(), "basement");
            connector.connect(storageCredentials);

            holder = getHolder(BasementLib.class, AbstractMariaHolder.class);
            holder.setConnector(connector);
            database = holder.createDatabase(storageCredentials.database()).ifNotExists(true).build().execReturn();
        }

        RedisCredentials redisCredentials = configuration.get(ConfigKeys.REDIS_CREDENTIALS);
        if (redisCredentials.enabled()) {
            getLogger().info("Loading redis...");
            redisManager = new DefaultRedisManager(redisCredentials);

            getLogger().info("Loading server manager...");
            serverManager = new DefaultServerManager(redisManager);

            getLogger().info("Loading remote services...");
            RRemoteService remoteService = redisManager.getRedissonClient().getRemoteService();

            try { velocityService = remoteService.get(RemoteVelocityService.class); } catch (Exception e) {
                getLogger().warn("Cannot load velocity service. (Ignore if you are not using BasementLib-Velocity)");
            }
            try { cerebrumService = remoteService.get(RemoteCerebrumService.class); } catch (Exception e) {
                getLogger().warn("Cannot load cerebrum service. (Ignore if you are not using Cerebrum)");
            }
        }

        registerListeners();

        registerCommands();

        BasementProvider.register(this);
        registerApiOnPlatform(this);
    }

    public void disable() {
        BasementProvider.unregister();

        if (holder != null) holder.close();
        if (redisManager != null) redisManager.getRedissonClient().shutdown();
    }

    @Override
    public PluginLogger getLogger() {
        return logger;
    }

    @Override
    public BasementConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public @Nullable RedisManager getRedisManager() {
        return redisManager;
    }

    @Override
    public @Nullable ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public @Nullable RemoteVelocityService getRemoteVelocityService() {
        return velocityService;
    }

    @Override
    public @Nullable RemoteCerebrumService getRemoteCerebrumService() {
        return cerebrumService;
    }

    @Override
    public @Nullable AbstractMariaDatabase getDatabase() {
        return database;
    }

    @Override
    public Connector createConnector(int minPoolSize, int maxPoolSize, String poolName) {
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

    public Path resolveConfig(String fileName, boolean create) {
        Path configFile = getConfigDirectory().resolve(fileName);
        // if the config doesn't exist, create it based on the template in the resources dir
        if (create && !Files.exists(configFile)) {
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException e) {
                // ignore
            }
            try (InputStream is = getResourceStream(fileName)) {
                Files.copy(is, configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return configFile;
    }

    public abstract Path getConfigDirectory();

    public abstract PluginLogger provideLogger();

    public abstract ConfigurationAdapter provideConfigurationAdapter(String configName, boolean create);

    protected abstract void registerApiOnPlatform(BasementLib basement);

    protected abstract void registerCommands();

    protected abstract void registerListeners();

}
