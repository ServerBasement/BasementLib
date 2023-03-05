package it.ohalee.basementlib.common.plugin;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.BasementProvider;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.plugin.BasementPlugin;
import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.api.redis.RedisCredentials;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.remote.RemoteCerebrumService;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.api.server.ServerManager;
import it.ohalee.basementlib.common.config.BasementConfiguration;
import it.ohalee.basementlib.common.config.ConfigKeys;
import it.ohalee.basementlib.common.persistence.maria.structure.MariaHolder;
import it.ohalee.basementlib.common.persistence.maria.structure.column.connector.MariaConnector;
import it.ohalee.basementlib.common.redis.DefaultRedisManager;
import it.ohalee.basementlib.common.server.DefaultServerManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.redisson.api.RRemoteService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public abstract class AbstractBasementPlugin implements BasementPlugin, BasementLib {

    private static final UUID uuid = UUID.randomUUID();
    protected ConfigurationAdapter adapter;
    protected BasementConfiguration configuration;
    private @Nullable RedisManager redisManager;
    private @Nullable ServerManager serverManager;
    private @Nullable RemoteVelocityService velocityService;
    private @Nullable RemoteCerebrumService cerebrumService;
    private @Nullable AbstractMariaDatabase database;
    private @Nullable AbstractMariaHolder holder;
    private PluginLogger logger;

    public final void load() {
        getLogger().info("Loading BasementLib...");
        logger = provideLogger();
    }

    public void enable() {
        getLogger().info("Loading configuration...");
        this.adapter = provideConfigurationAdapter(getConfigDirectory().resolve("config.yml").toFile(), true);
        this.configuration = new BasementConfiguration(this, adapter);

        StorageCredentials storageCredentials = configuration.get(ConfigKeys.MYSQL_CREDENTIALS);
        if (storageCredentials.enabled()) {
            getLogger().info("Loading database...");
            Connector connector = createConnector(storageCredentials.minIdleConnections(), storageCredentials.maxPoolSize(), "basement");
            connector.connect(storageCredentials);

            holder = new MariaHolder(connector);
            database = holder.createDatabase(storageCredentials.database()).ifNotExists(true).build().execReturn();
        } else {
            getLogger().warn("MySQL is disabled. Some features will not work. It's recommended to enable it.");
        }

        RedisCredentials redisCredentials = configuration.get(ConfigKeys.REDIS_CREDENTIALS);
        if (redisCredentials.enabled()) {
            getLogger().info("Loading redis...");
            redisManager = new DefaultRedisManager(redisCredentials);

            getLogger().info("Loading server manager...");
            serverManager = new DefaultServerManager(redisManager);

            getLogger().info("Loading remote services...");
            RRemoteService remoteService = redisManager.getRedissonClient().getRemoteService();

            try {
                velocityService = remoteService.get(RemoteVelocityService.class);
            } catch (Exception e) {
                getLogger().warn("Cannot load velocity service. (Ignore if you are not using BasementLib-Velocity)");
            }
            try {
                cerebrumService = remoteService.get(RemoteCerebrumService.class);
            } catch (Exception e) {
                getLogger().warn("Cannot load cerebrum service. (Ignore if you are not using Cerebrum)");
            }
        } else {
            getLogger().warn("Redis is disabled. Some features will not work. It's recommended to enable it.");
        }

        registerListeners();
        registerCommands();

        getLogger().info("Registering BasementLib provider (OTU: " + uuid + ")");
        BasementProvider.register(this);
        registerApiOnPlatform(this);

        getLogger().info("BasementLib loaded!");
    }

    public void disable() {
        getLogger().info("Unregistering BasementLib provider (OTU: " + uuid + ")");
        BasementProvider.unregister();

        if (holder != null) holder.close();
        if (redisManager != null) redisManager.getRedissonClient().shutdown();

        getLogger().info("BasementLib unloaded!");
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
    public @Nullable AbstractMariaDatabase getDatabase(String database) {
        if (holder == null) throw new IllegalStateException("Database is not enabled.");
        return holder.useDatabase(database);
    }

    @Override
    public void loadDatabase(AbstractMariaDatabase database) {
        if (holder == null) throw new IllegalStateException("Database is not enabled.");
        holder.loadDatabase(database);
    }

    @Override
    public Connector createConnector(int minPoolSize, int maxPoolSize, String poolName) {
        return new MariaConnector(minPoolSize, maxPoolSize, poolName);
    }

    @Override
    public @Nullable AbstractMariaHolder getHolder() {
        if (holder == null) throw new IllegalStateException("Database is not enabled.");
        return holder;
    }

    public Path resolveConfig(File file, boolean create) {
        Path configFile = file.toPath();
        // if the config doesn't exist, create it based on the template in the resources dir
        if (create && !Files.exists(configFile)) {
            try {
                Files.createDirectories(configFile.getParent());
            } catch (IOException e) {
                // ignore
            }
            try (InputStream is = getResourceStream(file.getName())) {
                Files.copy(is, configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return configFile;
    }

    public abstract PluginLogger provideLogger();

    protected abstract void registerApiOnPlatform(BasementLib basement);

    protected abstract void registerCommands();

    protected abstract void registerListeners();

}
