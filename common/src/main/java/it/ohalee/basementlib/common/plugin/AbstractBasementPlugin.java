package it.ohalee.basementlib.common.plugin;

import it.ohalee.basementlib.api.BasementLib;
import it.ohalee.basementlib.api.BasementProvider;
import it.ohalee.basementlib.api.plugin.BasementPlugin;

public abstract class AbstractBasementPlugin implements BasementPlugin {

    protected StandardBasement basement;

    public void init() {
        basement = new StandardBasement(this);
    }

    public void enable() {
        init();
        basement.start();

        BasementProvider.register(basement);

        registerApiOnPlatform(basement);
        registerCommands();
        registerListeners();
    }

    public void disable() {
        BasementProvider.unregister();
        basement.stop();
    }

    @Override
    public StandardBasement getBasement() {
        return basement;
    }

    protected abstract void registerApiOnPlatform(BasementLib basement);

    protected abstract void registerCommands();

    protected abstract void registerListeners();

}
