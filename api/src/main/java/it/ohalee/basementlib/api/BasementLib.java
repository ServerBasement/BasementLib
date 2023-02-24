package it.ohalee.basementlib.api;

import it.ohalee.basementlib.api.persistence.generic.Holder;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.remote.RemoteCerebrumService;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.api.server.ServerManager;

import java.util.UUID;

public interface BasementLib {

    /**
     * Gets the instance UUID
     *
     * @return the instance UUID
     */
    UUID getUuid();

    /**
     * Reload the configuration
     */
    void reloadConfig();

    /**
     * Gets the Redis Manager
     *
     * @return Redis Manager
     */
    RedisManager getRedisManager();

    /**
     * Gets the Server Manager
     *
     * @return Server Manager
     */
    ServerManager getServerManager();

    /**
     * Gets the remote instance of velocity service
     *
     * @return remote instance of velocity service
     */
    RemoteVelocityService getRemoteVelocityService();

    /**
     * Gets the remote instance of cerebrum service
     *
     * @return remote instance of cerebrum service
     */
    RemoteCerebrumService getRemoteCerebrumService();

    /**
     * Gets the default server database
     *
     * @return the default maria database
     */
    AbstractMariaDatabase getDatabase();

    /**
     * Gets a new Connector object
     *
     * @return connector object
     */
    Connector getConnector(int minPoolSize, int maxPoolSize, String poolName);

    <T extends Holder> T getHolder(Class<?> key, Class<T> type);

}
