package it.ohalee.basementlib.common.config;

import it.ohalee.basementlib.api.config.generic.KeyedConfiguration;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.plugin.BasementPlugin;

public class BasementConfiguration extends KeyedConfiguration {
    private final BasementPlugin plugin;

    public BasementConfiguration(BasementPlugin plugin, ConfigurationAdapter adapter) {
        super(adapter, ConfigKeys.getKeys());
        this.plugin = plugin;

        init();
    }

    @Override
    protected void load(boolean initial) {
        super.load(initial);
    }

    @Override
    public void reload() {
        super.reload();
    }

    public BasementPlugin getPlugin() {
        return this.plugin;
    }
}
