package it.ohalee.basementlib.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import it.ohalee.basementlib.velocity.commands.CreateServerCommand;
import it.ohalee.basementlib.velocity.listeners.PlayerListener;
import it.ohalee.basementlib.velocity.redis.handler.BukkitNotifyShutdownHandler;
import it.ohalee.basementlib.velocity.redis.handler.ServerShutdownHandler;
import it.ohalee.basementlib.velocity.remote.RemoteVelocityServiceImpl;
import lombok.Getter;
import org.redisson.api.RRemoteService;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Plugin(
        id = "basementlib",
        name = "BasementLib",
        version = "1.0",
        authors = {"ohAlee"}
)
@Getter
public class BasementVelocity extends AbstractBasementPlugin {

    private final ProxyServer server;
    private final File dataFolder;
    private final File configFile;
    private final Logger logger;

    @Inject
    public BasementVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;

        dataFolder = dataDirectory.toFile();
        configFile = new File(dataDirectory.toFile(), "config.yml");
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onInitialize(ProxyInitializeEvent event) {
        enable();

        basement.getRedisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(server));
        basement.getRedisManager().registerTopicListener(BukkitNotifyShutdownMessage.TOPIC, new BukkitNotifyShutdownHandler(server));
        RRemoteService remoteService = basement.getRedisManager().getRedissonClient().getRemoteService();
        remoteService.register(RemoteVelocityService.class, new RemoteVelocityServiceImpl(this), 3, Executors.newSingleThreadExecutor());

        // TODO: 23/02/2023 cerebrum can not exist
        server.getCommandManager().register(server.getCommandManager().metaBuilder("createserver").aliases("cs").build(), new CreateServerCommand(this));

        server.getEventManager().register(this, new PlayerListener(this));

        basement.getRedisManager().publishMessage(new VelocityNotifyMessage(false));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        basement.getRedisManager().publishMessage(new VelocityNotifyMessage(true));
        disable();
    }

    @Override
    protected void registerApiOnPlatform(BasementLib basement) {
        //Velocity doesn't provide a service manager
    }

    @Override
    protected void registerCommands() {

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public File getConfig() {
        return configFile;
    }

}
