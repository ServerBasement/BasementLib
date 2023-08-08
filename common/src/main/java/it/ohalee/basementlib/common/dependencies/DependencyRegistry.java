package it.ohalee.basementlib.common.dependencies;

import com.google.gson.JsonElement;
import it.ohalee.basementlib.common.plugin.bootstrap.BasementBootstrap;

/**
 * Applies DataHistory specific behaviour for {@link Dependency}s.
 */
public class DependencyRegistry {

    private final BasementBootstrap plugin;

    public DependencyRegistry(BasementBootstrap plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isGsonRelocated() {
        return JsonElement.class.getName().startsWith("it.ohalee");
    }

    public boolean shouldAutoLoad(Dependency dependency) {
        switch (dependency) {
            // all used within 'isolated' classloaders, and are therefore not
            // relocated.
            case ASM:
            case ASM_COMMONS:
            case JAR_RELOCATOR:
            case H2_DRIVER_LEGACY:
                return false;
            default:
                return true;
        }
    }

}
