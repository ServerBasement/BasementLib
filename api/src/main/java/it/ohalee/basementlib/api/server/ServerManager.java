package it.ohalee.basementlib.api.server;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ServerManager {

    /**
     * Add a new Server
     *
     * @param name   the server name
     * @param server the server object
     */
    void addServer(String name, BukkitServer server);

    /**
     * Remove an existing Server
     *
     * @param name the server name
     */
    void removeServer(String name);

    /**
     * Gets all servers
     *
     * @return all registered servers
     */
    Collection<BukkitServer> getServers();

    /**
     * Gets the server object by name
     *
     * @param name server name
     * @return server object
     */
    Optional<BukkitServer> getServer(String name);

    /**
     * Gets if a server is online
     *
     * @param name server name
     * @return true if is online, false otherwise
     */
    boolean isOnline(String name);

    /**
     * Gets if a server is registered
     *
     * @param name server name
     * @return true if server is registered, false otherwise
     */
    boolean exists(String name);

    /**
     * Gets all online servers
     *
     * @return all online servers
     */
    List<BukkitServer> getOnlineServers();

    /**
     * Gets all online servers that name starts with {@code startsWith}
     *
     * @param startsWith the initial server name
     * @return a list of servers that name starts with {@code startsWith}
     */
    List<BukkitServer> getOnlineServers(String startsWith);

    /**
     * Gets the total number of online players
     *
     * @return how many players are online in the servers
     */
    int getOnlinePlayers();

    /**
     * Gets the total number of online players that name starts with {@code startsWith}
     *
     * @param startsWith the initial server name
     * @return how many players are online in the servers
     */
    int getOnlinePlayers(String startsWith);

    /**
     * It's called when a new {@link BukkitServer} is added
     *
     * @param server the server object
     */
    void onServerAdd(BukkitServer server);

    /**
     * It's called when an existing {@link BukkitServer} is removed
     *
     * @param server the server object
     */
    void onServerRemove(BukkitServer server);

    /**
     * Sets a new consumer that is accepted on {@link ServerManager#onServerAdd(BukkitServer)}
     *
     * @param serverAddConsumer the consumer
     */
    void setServerAddConsumer(Consumer<BukkitServer> serverAddConsumer);

    /**
     * Sets a new consumer that is accepted on {@link ServerManager#onServerRemove(BukkitServer)}
     *
     * @param serverRemoveConsumer the consumer
     */
    void setServerRemoveConsumer(Consumer<BukkitServer> serverRemoveConsumer);
}
