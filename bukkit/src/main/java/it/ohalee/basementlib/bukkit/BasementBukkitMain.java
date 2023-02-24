package it.ohalee.basementlib.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BasementBukkitMain extends JavaPlugin {

    private BasementBukkitPlugin basementPlugin;

    @Override
    public void onEnable() {
        basementPlugin = new BasementBukkitPlugin(this);
        basementPlugin.enable();
    }

    @Override
    public void onDisable() {
        basementPlugin.disable();
    }
}
