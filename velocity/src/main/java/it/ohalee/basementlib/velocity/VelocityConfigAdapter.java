package it.ohalee.basementlib.velocity;

import it.ohalee.basementlib.api.config.generic.adapter.ConfigurateConfigAdapter;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.plugin.BasementPlugin;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.nio.file.Path;

public class VelocityConfigAdapter extends ConfigurateConfigAdapter implements ConfigurationAdapter {

    public VelocityConfigAdapter(BasementPlugin plugin, Path path) {
        super(plugin, path);
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return YAMLConfigurationLoader.builder().setPath(path).build();
    }

}
