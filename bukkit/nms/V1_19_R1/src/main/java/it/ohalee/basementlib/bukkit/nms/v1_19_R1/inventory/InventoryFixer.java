package it.ohalee.basementlib.bukkit.nms.v1_19_R1.inventory;

import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

// VIA VERSION FIXER :D
public class InventoryFixer implements Listener {

    public InventoryFixer(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            EntityPlayer handle = ((CraftPlayer) event.getView().getPlayer()).getHandle();
            handle.b.a(new PacketPlayOutSetSlot(-1, handle.bU.k(), -1, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR))));
        }
    }

}
