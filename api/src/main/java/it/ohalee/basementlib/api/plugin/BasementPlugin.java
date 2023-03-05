package it.ohalee.basementlib.api.plugin;

import it.ohalee.basementlib.api.config.generic.KeyedConfiguration;
import it.ohalee.basementlib.api.config.generic.adapter.ConfigurationAdapter;
import it.ohalee.basementlib.api.plugin.logging.PluginLogger;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface BasementPlugin {

    /**
     * Gets a wrapped logger instance for the platform.
     *
     * @return the plugin's logger
     */
    PluginLogger getLogger();

    /**
     * Gets the configuration's name.
     *
     * @return the configuration's name
     */
    KeyedConfiguration getConfiguration();

    /**
     * Gets the plugins main data storage directory
     *
     * @return the platforms data folder
     */
    Path getDataDirectory();

    /**
     * Gets the plugins configuration directory
     *
     * @return the config directory
     */
    default Path getConfigDirectory() {
        return getDataDirectory();
    }

    /**
     * Gets a bundled resource file from the jar
     *
     * @param path the path of the file
     * @return the file as an input stream
     */
    default InputStream getResourceStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    /**
     * Gets a configuration adapter for the given path
     *
     * @param file the file to load
     * @return the configuration adapter
     */
    ConfigurationAdapter provideConfigurationAdapter(File file, boolean create);
}
