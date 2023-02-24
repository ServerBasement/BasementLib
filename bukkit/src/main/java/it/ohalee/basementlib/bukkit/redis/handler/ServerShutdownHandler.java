package it.ohalee.basementlib.bukkit.redis.handler;

import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "basement")
@RequiredArgsConstructor
public class ServerShutdownHandler implements BasementMessageHandler<ServerShutdownMessage> {

    private final BasementBukkit basement;

    @Override
    public void execute(ServerShutdownMessage message) {
        if (!message.getReceiver().equals(basement.getServerID())) return;
        basement.getPlugin().getServer().shutdown();
    }

    @Override
    public Class<ServerShutdownMessage> getCommandClass() {
        return ServerShutdownMessage.class;
    }
}
