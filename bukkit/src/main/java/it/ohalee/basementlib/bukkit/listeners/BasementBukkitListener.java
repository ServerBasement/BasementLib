package it.ohalee.basementlib.bukkit.listeners;

import it.ohalee.basementlib.bukkit.BasementBukkitPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class BasementBukkitListener implements Listener {

    private final BasementBukkitPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.plugin.globalDatabase() != null)
            this.plugin.globalDatabase().registerPlayer(event.getPlayer().getName(), event.getPlayer().getUniqueId());
    }

}
