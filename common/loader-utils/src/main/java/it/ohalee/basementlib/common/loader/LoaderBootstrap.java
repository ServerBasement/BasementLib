package it.ohalee.basementlib.common.loader;

/**
 * Minimal bootstrap plugin, called by the loader plugin.
 */
public interface LoaderBootstrap {

    void onLoad();

    default void onEnable() {
    }

    default void onDisable() {
    }

}
