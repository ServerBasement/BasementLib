/*
 *
 * This file is part of servercore - servercore.velocity.main | VelocityConfigAdapter.java
 *
 *  Copyright (c) ohAlee (Ale) <alebartoh@gmail.com>
 *  Copyright (c) 2021-2022.
 *
 *  You can't use this code without the owner permission.
 */

package it.ohalee.basementlib.velocity;

import it.ohalee.basementlib.api.config.generic.adapter.ConfigurateConfigAdapter;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.nio.file.Path;

public class VelocityConfigAdapter extends ConfigurateConfigAdapter implements ConfigurationAdapter {

    private final BasementVelocity plugin;
    private final Path path;

    public VelocityConfigAdapter(BasementVelocity plugin, Path path) {
        super(plugin, path);
        this.plugin = plugin;
        this.path = path;
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return YAMLConfigurationLoader.builder().setPath(path).build();
    }

    @Override
    public Object set(String path, Object obj, boolean save) {
        ConfigurationNode node = resolvePath(path);
        node.setValue(obj);
        if (save) {
            save(node);
            reload();
        }
        return obj;
    }

    @Override
    public void update(Class<?> loader) {
        this.plugin.update(loader, path.toFile().getName(), this);
    }
}
