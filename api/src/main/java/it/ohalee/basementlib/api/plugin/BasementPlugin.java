package it.ohalee.basementlib.api.plugin;

import it.ohalee.basementlib.api.BasementLib;

import java.io.File;

public interface BasementPlugin {

    /**
     * Gets the basement instance
     *
     * @return basement instance
     */
    BasementLib getBasement();

    /**
     * Gets the config file
     *
     * @return config file
     */
    File getConfig();
}
