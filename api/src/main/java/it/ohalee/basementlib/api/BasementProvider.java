package it.ohalee.basementlib.api;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

public final class BasementProvider {
    private static BasementLib instance = null;

    @ApiStatus.Internal
    private BasementProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Gets an instance of the {@link BasementLib} API,
     * throwing {@link IllegalStateException} if the API is not loaded yet.
     *
     * <p>This method will never return null.</p>
     *
     * @return an instance of the Basement API
     * @throws IllegalStateException if the API is not loaded yet
     */
    public static @NonNull BasementLib get() {
        BasementLib instance = BasementProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    @ApiStatus.Internal
    public static void register(BasementLib instance) {
        BasementProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        BasementProvider.instance = null;
    }

    /**
     * Exception thrown when the API is requested before it has been loaded.
     */
    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = "The Basement API isn't loaded yet!\n" +
                "This could be because:\n" +
                "  a) the Basement plugin is not installed or it failed to enable\n" +
                "  b) the plugin in the stacktrace does not declare a dependency on Basement\n" +
                "  c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase\n" +
                "     (call the #get method in onEnable, not the constructor!)";

        NotLoadedException() {
            super(MESSAGE);
        }
    }
}
