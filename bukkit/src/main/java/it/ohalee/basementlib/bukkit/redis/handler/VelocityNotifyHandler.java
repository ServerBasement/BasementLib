package it.ohalee.basementlib.bukkit.redis.handler;

import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import it.ohalee.basementlib.bukkit.BasementBukkitPlugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VelocityNotifyHandler implements BasementMessageHandler<VelocityNotifyMessage> {

    private final BasementBukkitPlugin basement;

    @Override
    public void execute(VelocityNotifyMessage message) {
        if (message.isShutdown()) return;
        if (basement.getRemoteVelocityService() != null)
            basement.getRemoteVelocityService().registerServer(basement.getServerID(), basement.getPlugin().getServer().getPort());
    }

    @Override
    public Class<VelocityNotifyMessage> getCommandClass() {
        return VelocityNotifyMessage.class;
    }
}
