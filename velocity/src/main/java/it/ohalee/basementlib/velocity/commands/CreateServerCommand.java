package it.ohalee.basementlib.velocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import it.ohalee.basementlib.velocity.BasementVelocity;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RequiredArgsConstructor
public class CreateServerCommand implements SimpleCommand {

    private final BasementVelocity velocity;

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length < 1) {
            invocation.source().sendMessage(Component.text("/createserver <server-name>", NamedTextColor.YELLOW));
            return;
        }
        velocity.getBasement().getRemoteCerebrumService().createServer(invocation.arguments()[0]);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("basement.command.createserver");
    }

}
