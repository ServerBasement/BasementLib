package it.ohalee.basementlib.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BasementBukkitMain extends JavaPlugin {

    private final BasementBukkitPlugin basementPlugin;

    public BasementBukkitMain() {
        basementPlugin = new BasementBukkitPlugin(this);
    }

    @Override
    public void onLoad() {
        basementPlugin.load();
    }

    @Override
    public void onEnable() {
        basementPlugin.enable();
    }

    @Override
    public void onDisable() {
        basementPlugin.disable();
    }
}
