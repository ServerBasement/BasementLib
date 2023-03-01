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
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.api.redis.messages.implementation.BukkitNotifyShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.ServerShutdownMessage;
import it.ohalee.basementlib.api.redis.messages.implementation.VelocityNotifyMessage;
import it.ohalee.basementlib.api.remote.RemoteVelocityService;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import it.ohalee.basementlib.common.plugin.logging.Slf4jPluginLogger;
import it.ohalee.basementlib.velocity.commands.CreateServerCommand;
import it.ohalee.basementlib.velocity.listeners.PlayerListener;
import it.ohalee.basementlib.velocity.redis.handler.BukkitNotifyShutdownHandler;
import it.ohalee.basementlib.velocity.redis.handler.ServerShutdownHandler;
import it.ohalee.basementlib.velocity.remote.RemoteVelocityServiceImpl;
import lombok.Getter;
import org.redisson.api.RRemoteService;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.Executors;

@Plugin(
        id = "basementlib",
        name = "BasementLib",
        version = "1.0",
        authors = {"ohAlee"}
)
@Getter
public class BasementVelocity extends AbstractBasementPlugin {

    @Inject
    private ProxyServer server;

    @Inject
    @DataDirectory
    private Path configDirectory;
    private final Logger logger;

    @Inject
    public BasementVelocity(Logger logger) {
        this.logger = logger;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onInitialize(ProxyInitializeEvent event) {
        load();
        enable();

        if (getRedisManager() != null) {
            getRedisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(server));
            getRedisManager().registerTopicListener(BukkitNotifyShutdownMessage.TOPIC, new BukkitNotifyShutdownHandler(server));

            RRemoteService remoteService = getRedisManager().getRedissonClient().getRemoteService();
            remoteService.register(RemoteVelocityService.class, new RemoteVelocityServiceImpl(this), 3, Executors.newSingleThreadExecutor());

            server.getCommandManager().register(server.getCommandManager().metaBuilder("createserver").aliases("cs").build(), new CreateServerCommand(this));

            getRedisManager().publishMessage(new VelocityNotifyMessage(false));

            server.getEventManager().register(this, new PlayerListener(this));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (getRedisManager() != null) getRedisManager().publishMessage(new VelocityNotifyMessage(true));
        disable();
    }

    @Override
    public Path getDataDirectory() {
        return this.configDirectory.toAbsolutePath();
    }

    @Override
    public PluginLogger provideLogger() {
        return new Slf4jPluginLogger(logger);
    }

    @Override
    public ConfigurationAdapter provideConfigurationAdapter(String configName, boolean create) {
        return new VelocityConfigAdapter(this, resolveConfig(configName, create));
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

}
