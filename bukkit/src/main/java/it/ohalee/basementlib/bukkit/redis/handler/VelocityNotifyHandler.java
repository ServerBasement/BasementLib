package it.ohalee.basementlib.bukkit.redis.handler;

import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VelocityNotifyHandler implements BasementMessageHandler<VelocityNotifyMessage> {

    private final BasementBukkit basement;

    @Override
    public void execute(VelocityNotifyMessage message) {
        if (message.isShutdown()) return;
        basement.getRemoteVelocityService().registerServer(basement.getServerID(), basement.getPlugin().getServer().getPort());
    }

    @Override
    public Class<VelocityNotifyMessage> getCommandClass() {
        return VelocityNotifyMessage.class;
    }
}
