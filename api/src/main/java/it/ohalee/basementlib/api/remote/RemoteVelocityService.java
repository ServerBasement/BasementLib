package it.ohalee.basementlib.api.remote;

import java.util.UUID;

public interface RemoteVelocityService {

    /**
     * Gets if a player is in a server that names start with {@param server}
     *
     * @param player the player name
     * @param server the server ranch name
     * @return true if player is in a server ranch, false otherwise
     */
    boolean isOnRanch(String player, String server);

    /**
     * Gets if a player is in a server that names start with {@param server}
     *
     * @param player the player uuid
     * @param server the server ranch name
     * @return true if player is in a server ranch, false otherwise
     */
    boolean isOnRanch(UUID player, String server);

    /**
     * Gets the server name of a player
     *
     * @param player the player name
     * @return the server name
     */
    String getServer(String player);

    /**
     * Send a player to a server
     *
     * @param player the player name
     * @param server the server name
     */
    void sendToServer(String player, String server);

    /**
     * Send a player to a server
     *
     * @param uuid   the player uuid
     * @param server the server name
     */
    void sendToServer(UUID uuid, String server);

    /**
     * Send same messages to a player
     *
     * @param player   player name
     * @param messages the messages to send to the player
     */
    void sendMessage(String player, String... messages);

    /**
     * Send same messages to a player only if it has a determinate permission
     *
     * @param player         player name
     * @param permissionNode the permission the player need to receive the messages
     * @param messages       the messages to send to the player
     */
    void sendMessageWithPermission(String player, String permissionNode, String... messages);

    /**
     * Send same messages to a player, serialized in json string
     *
     * @param player   player name
     * @param messages the messages to send to the player serialized in Json String
     */
    void sendMessageComponent(String player, String... messages);

    /**
     * Register a new server to velocity
     *
     * @param serverName the server name
     * @param port       the server port
     */
    void registerServer(String serverName, int port);

}
