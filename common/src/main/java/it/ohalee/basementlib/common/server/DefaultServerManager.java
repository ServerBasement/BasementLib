package it.ohalee.basementlib.common.server;

import it.ohalee.basementlib.api.redis.RedisManager;
import it.ohalee.basementlib.api.server.BukkitServer;
import it.ohalee.basementlib.api.server.ServerManager;
import org.redisson.api.RMapCache;
import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.EntryRemovedListener;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultServerManager implements ServerManager {

    private final RMapCache<String, BukkitServer> servers;

    private Consumer<BukkitServer> serverAddConsumer = server -> {
    };
    private Consumer<BukkitServer> serverRemoveConsumer = server -> {
    };

    public DefaultServerManager(RedisManager redisManager) {
        servers = redisManager.redissonClient().getMapCache("servers");
        servers.addListener((EntryCreatedListener<String, BukkitServer>) event -> onServerAdd(event.getValue()));
        servers.addListener((EntryRemovedListener<String, BukkitServer>) event -> onServerRemove(event.getValue()));
        servers.addListener((EntryExpiredListener<String, BukkitServer>) event -> onServerRemove(event.getValue()));
    }

    @Override
    public void addServer(String name, BukkitServer server) {
        servers.fastPut(name, server, 1, TimeUnit.MINUTES);
    }

    @Override
    public void removeServer(String name) {
        servers.fastRemove(name);
    }

    @Override
    public Collection<BukkitServer> getServers() {
        return Collections.unmodifiableCollection(servers.values());
    }

    @Override
    public Optional<BukkitServer> getServer(String name) {
        return Optional.ofNullable(servers.get(name));
    }

    @Override
    public boolean isOnline(String name) {
        BukkitServer server = servers.get(name);
        if (server == null) return false;
        return server.isServerOnline();
    }

    @Override
    public boolean exists(String name) {
        return servers.containsKey(name);
    }

    @Override
    public List<BukkitServer> getOnlineServers() {
        List<BukkitServer> onlineServers = new ArrayList<>();
        for (BukkitServer server : servers.values()) {
            if (server.isServerOnline()) onlineServers.add(server);
        }
        return onlineServers;
    }

    @Override
    public List<BukkitServer> getOnlineServers(String startsWith) {
        String lower = startsWith.toLowerCase();
        return getOnlineServers().parallelStream().filter(server -> server.getName().toLowerCase().startsWith(lower)).collect(Collectors.toList());
    }

    @Override
    public int getOnlinePlayers() {
        return getOnlineServers().parallelStream().mapToInt(BukkitServer::getOnline).sum();
    }

    @Override
    public int getOnlinePlayers(String startsWith) {
        String lower = startsWith.toLowerCase();
        return getOnlineServers().parallelStream().filter(server -> server.getName().toLowerCase().startsWith(lower)).mapToInt(BukkitServer::getOnline).sum();
    }

    @Override
    public void onServerAdd(BukkitServer server) {
        serverAddConsumer.accept(server);
    }

    @Override
    public void onServerRemove(BukkitServer server) {
        serverRemoveConsumer.accept(server);
    }

    @Override
    public void setServerAddConsumer(Consumer<BukkitServer> consumer) {
        this.serverAddConsumer = consumer;
    }

    @Override
    public void setServerRemoveConsumer(Consumer<BukkitServer> consumer) {
        this.serverRemoveConsumer = consumer;
    }
}
