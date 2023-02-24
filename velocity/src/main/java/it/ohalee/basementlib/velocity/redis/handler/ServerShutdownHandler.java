package it.ohalee.basementlib.velocity.redis.handler;

import com.velocitypowered.api.proxy.ProxyServer;
import it.ohalee.basementlib.api.redis.messages.handler.BasementMessageHandler;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@Slf4j(topic = "basement")
@RequiredArgsConstructor
public class ServerShutdownHandler implements BasementMessageHandler<ServerShutdownMessage> {

    private final ProxyServer server;

    @Override
    public void execute(ServerShutdownMessage message) {
        if (!message.getReceiver().endsWith("velocity")) return;
        log.info("Il server si sta spegnendo per il ServerShutdownMessage inviato da " + message.getSender());
        server.shutdown(Component.text()
                .append(Component.text("Riavvio del server").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD))
                .append(Component.newline())
                .append(Component.text("Torneremo online tra pochi secondi!").color(NamedTextColor.LIGHT_PURPLE)).build());
    }

    @Override
    public Class<ServerShutdownMessage> getCommandClass() {
        return ServerShutdownMessage.class;
    }
}
