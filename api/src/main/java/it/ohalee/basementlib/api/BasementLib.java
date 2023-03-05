package it.ohalee.basementlib.api;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.remote.RemoteCerebrumService;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.api.server.ServerManager;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface BasementLib {

    /**
     * Gets the instance UUID
     *
     * @return the instance UUID
     */
    UUID getUuid();

    /**
     * Gets the Redis Manager
     *
     * @return Redis Manager
     */
    @Nullable RedisManager getRedisManager();

    /**
     * Gets the Server Manager
     *
     * @return Server Manager
     */
    @Nullable ServerManager getServerManager();

    /**
     * Gets the remote instance of velocity service
     *
     * @return remote instance of velocity service
     */
    @Nullable RemoteVelocityService getRemoteVelocityService();

    /**
     * Gets the remote instance of cerebrum service
     *
     * @return remote instance of cerebrum service
     */
    @Nullable RemoteCerebrumService getRemoteCerebrumService();

    /**
     * Gets the default server database
     *
     * @return the default maria database
     */
    @Nullable AbstractMariaDatabase getDatabase();

    /**
     * Gets a database by name
     *
     * @param database the database name
     * @return the database
     */
    @Nullable AbstractMariaDatabase getDatabase(String database);

    /**
     * Loads the a database
     *
     * @param database the database
     * @see QueryBuilderCreateDatabase#exec() To create and/or load a database
     */
    void loadDatabase(AbstractMariaDatabase database);

    /**
     * Gets a new Connector object
     *
     * @return connector object
     */
    Connector createConnector(int minPoolSize, int maxPoolSize, String poolName);

    /**
     * Gets a holder instance
     *
     * @param key  the key
     * @param type the type
     * @param <T>  the type
     * @return the holder instance
     */
    @Nullable AbstractMariaHolder getHolder();

}
