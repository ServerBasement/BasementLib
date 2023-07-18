package it.ohalee.basementlib.api;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.LocalFactory;
import it.ohalee.basementlib.api.plugin.BasementPlugin;
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
    UUID uuid();

    /**
     * Gets the plugin
     *
     * @return the plugin
     */
    BasementPlugin plugin();

    /**
     * Gets the Redis Manager
     *
     * @return Redis Manager
     */
    @Nullable RedisManager redisManager();

    /**
     * Gets the Server Manager
     *
     * @return Server Manager
     */
    @Nullable ServerManager serverManager();

    /**
     * Gets the remote instance of velocity service
     *
     * @return remote instance of velocity service
     */
    @Nullable RemoteVelocityService remoteVelocityService();

    /**
     * Gets the remote instance of cerebrum service
     *
     * @return remote instance of cerebrum service
     */
    @Nullable RemoteCerebrumService remoteCerebrumService();

    /**
     * Gets a new Connector object
     *
     * @return connector object
     */
    Connector createConnector(TypeConnector connector, int minPoolSize, int maxPoolSize, String poolName);

    AbstractSqlDatabase h2(String database);

    AbstractSqlDatabase maria();

    /**
     * Gets the database provider
     *
     * @param type     the database type
     * @param database the database name
     * @return the database provider
     * @see TypeConnector
     */
    AbstractSqlDatabase database(TypeConnector type, String database);
}
