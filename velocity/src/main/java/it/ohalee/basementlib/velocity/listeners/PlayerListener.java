package it.ohalee.basementlib.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import it.ohalee.basementlib.velocity.BasementVelocity;
import org.redisson.api.RAtomicLong;

public class PlayerListener {

    private final BasementVelocity velocity;
    private final RAtomicLong playersCount;

    public PlayerListener(BasementVelocity velocity) {
        this.velocity = velocity;
        playersCount = velocity.redisManager().redissonClient().getAtomicLong("playersCount");
        playersCount.set(velocity.getServer().getPlayerCount());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLogged(PostLoginEvent event) {
        playersCount.set(velocity.getServer().getPlayerCount());
    }

    @Subscribe(order = PostOrder.LAST)
    private void onQuit(DisconnectEvent event) {
        playersCount.set(velocity.getServer().getPlayerCount());
    }

}