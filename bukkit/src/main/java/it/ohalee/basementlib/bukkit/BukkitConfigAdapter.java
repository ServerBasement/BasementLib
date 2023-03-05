/*
 *
 * This file is part of servercore - servercore.bukkit.main | BukkitConfigAdapter.java
 *
 *  Copyright (c) ohAlee (Ale) <alebartoh@gmail.com>
 *  Copyright (c) 2021-2022.
 *
 *  You can't use this code without the owner permission.
 */
package it.ohalee.basementlib.bukkit;

import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitConfigAdapter implements ConfigurationAdapter {
    private final BasementBukkitPlugin plugin;
    private final File file;
    private YamlConfiguration configuration;

    public BukkitConfigAdapter(BasementBukkitPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        reload();
    }

    @Override
    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getString(String path, String def) {
        return this.configuration.getString(path, def);
    }

    @Override
    public int getInteger(String path, int def) {
        return this.configuration.getInt(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return this.configuration.getBoolean(path, def);
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        List<String> list = this.configuration.getStringList(path);
        return list == null ? def : list;
    }

    @Override
    public Map<String, String> getStringMap(String path, Map<String, String> def) {
        Map<String, String> map = new HashMap<>();
        ConfigurationSection section = this.configuration.getConfigurationSection(path);
        if (section == null) {
            return def;
        }

        for (String key : section.getKeys(false)) {
            map.put(key, section.getString(key));
        }

        return map;
    }

    @Override
    public Object get(String path, Object def) {
        return this.configuration.get(path, def);
    }

    @Override
    public Object set(String path, Object obj) {
        this.configuration.set(path, obj);
        save();
        reload();
        return obj;
    }

    @Override
    public BasementBukkitPlugin getPlugin() {
        return this.plugin;
    }
}