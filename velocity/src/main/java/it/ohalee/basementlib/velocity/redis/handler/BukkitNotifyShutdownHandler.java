package it.ohalee.basementlib.velocity.redis.handler;

import com.velocitypowered.api.proxy.ProxyServer;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import it.ohalee.basementlib.common.config.ConfigKeys;
import it.ohalee.basementlib.velocity.BasementVelocity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BukkitNotifyShutdownHandler implements BasementMessageHandler<BukkitNotifyShutdownMessage> {

    private final BasementVelocity velocity;
    private final ProxyServer server;

    @Override
    public void execute(BukkitNotifyShutdownMessage message) {
        if (!velocity.configuration().get(ConfigKeys.AUTOMATIC_REGISTER_SERVER)) return;

        server.getServer(message.getServerId()).ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo()));
    }

    @Override
    public Class<BukkitNotifyShutdownMessage> getCommandClass() {
        return BukkitNotifyShutdownMessage.class;
    }
}
