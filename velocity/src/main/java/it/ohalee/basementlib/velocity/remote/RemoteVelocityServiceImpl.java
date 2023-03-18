package it.ohalee.basementlib.velocity.remote;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.velocity.BasementVelocity;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RemoteVelocityServiceImpl implements RemoteVelocityService {

    private final GsonComponentSerializer componentSerializer = GsonComponentSerializer.colorDownsamplingGson();

    private final BasementVelocity velocity;

    @Override
    public boolean isOnRanch(String player, String server) {
        Optional<Player> proxyPlayer = velocity.getServer().getPlayer(player);
        if (!proxyPlayer.isPresent()) return false;
        Optional<ServerConnection> connection = proxyPlayer.get().getCurrentServer();
        return connection.map(serverConnection -> serverConnection.getServerInfo().getName().startsWith(server)).orElse(false);
    }

    @Override
    public boolean isOnRanch(UUID player, String server) {
        Optional<Player> proxyPlayer = velocity.getServer().getPlayer(player);
        if (!proxyPlayer.isPresent()) return false;
        Optional<ServerConnection> connection = proxyPlayer.get().getCurrentServer();
        return connection.map(serverConnection -> serverConnection.getServerInfo().getName().startsWith(server)).orElse(false);
    }

    @Override
    public String getServer(String player) {
        Optional<Player> proxyPlayer = velocity.getServer().getPlayer(player);
        if (!proxyPlayer.isPresent()) return null;
        Optional<ServerConnection> connection = proxyPlayer.get().getCurrentServer();
        return connection.map(serverConnection -> serverConnection.getServerInfo().getName()).orElse(null);
    }

    @Override
    public void sendToServer(String player, String server) {
        Optional<Player> optionalPlayer = this.velocity.getServer().getPlayer(player);
        if (!optionalPlayer.isPresent()) return;
        Optional<RegisteredServer> optionalServer = this.velocity.getServer().getServer(server);
        if (!optionalServer.isPresent()) {
            optionalPlayer.get().sendMessage(Component.text("Il server a cui stai provando ad accedere è offline!", NamedTextColor.RED));
            return;
        }

        Player realPlayer = optionalPlayer.get();
        Optional<ServerConnection> currentServer = realPlayer.getCurrentServer();
        if (!currentServer.isPresent() || currentServer.get().getServerInfo().getName().equalsIgnoreCase(server))
            return;
        realPlayer.createConnectionRequest(optionalServer.get()).fireAndForget();
    }

    @Override
    public void sendToServer(UUID uuid, String server) {
        Optional<Player> optionalPlayer = this.velocity.getServer().getPlayer(uuid);
        if (!optionalPlayer.isPresent()) return;
        Optional<RegisteredServer> optionalServer = this.velocity.getServer().getServer(server);
        if (!optionalServer.isPresent()) {
            optionalPlayer.get().sendMessage(Component.text("Il server a cui stai provando ad accedere è offline!", NamedTextColor.RED));
            return;
        }

        Player realPlayer = optionalPlayer.get();
        Optional<ServerConnection> currentServer = realPlayer.getCurrentServer();
        if (!currentServer.isPresent() || currentServer.get().getServerInfo().getName().equalsIgnoreCase(server))
            return;
        realPlayer.createConnectionRequest(optionalServer.get()).fireAndForget();
    }

    @Override
    public void sendMessage(String player, String... messages) {
        Optional<Player> optionalPlayer = this.velocity.getServer().getPlayer(player);
        if (!optionalPlayer.isPresent()) return;
        Player velocityPlayer = optionalPlayer.get();
        for (String message : messages) {
            velocityPlayer.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    @Override
    public void sendMessageWithPermission(String player, String permissionNode, String... messages) {
        Optional<Player> optionalPlayer = this.velocity.getServer().getPlayer(player);
        if (!optionalPlayer.isPresent()) return;
        Player velocityPlayer = optionalPlayer.get();
        if (!velocityPlayer.hasPermission(permissionNode)) return;
        for (String message : messages) {
            velocityPlayer.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    @Override
    public void sendMessageComponent(String player, String... messages) {
        Optional<Player> optionalPlayer = this.velocity.getServer().getPlayer(player);
        if (!optionalPlayer.isPresent()) return;
        Player velocityPlayer = optionalPlayer.get();
        for (String message : messages) {
            velocityPlayer.sendMessage(componentSerializer.deserialize(message));
        }
    }

    @Override
    public void registerServer(String serverName, int port) {
        Optional<RegisteredServer> serverOptional = velocity.getServer().getServer(serverName);
        ServerInfo newServer = new ServerInfo(serverName, new InetSocketAddress(serverName, port));
        if (serverOptional.isPresent()) {
            velocity.getServer().unregisterServer(serverOptional.get().getServerInfo());
            velocity.getServer().registerServer(newServer);
        } else {
            velocity.getServer().registerServer(newServer);
        }
    }

}
