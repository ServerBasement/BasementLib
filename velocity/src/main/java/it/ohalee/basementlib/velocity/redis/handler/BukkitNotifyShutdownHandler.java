package it.ohalee.basementlib.velocity.redis.handler;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BukkitNotifyShutdownHandler implements BasementMessageHandler<BukkitNotifyShutdownMessage> {

    private final ProxyServer server;

    @Override
    public void execute(BukkitNotifyShutdownMessage message) {
        Optional<RegisteredServer> optionalRegisteredServer = server.getServer(message.getServerId());
        optionalRegisteredServer.ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo()));
    }

    @Override
    public Class<BukkitNotifyShutdownMessage> getCommandClass() {
        return BukkitNotifyShutdownMessage.class;
    }
}
