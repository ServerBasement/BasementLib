package it.ohalee.basementlib.api.config.generic.adapter;

import it.ohalee.basementlib.api.plugin.BasementPlugin;

import java.util.List;
import java.util.Map;

/**
 * The ConfigurationAdapter interface represents an adapter for accessing and manipulating configuration data.
 */
public interface ConfigurationAdapter {

    Object set(String path, Object obj, boolean save);

    /**
     * Retrieves the BasementPlugin instance.
     *
     * @return The BasementPlugin instance.
     */
    BasementPlugin getPlugin();

    /**
     * Reloads the plugin.
     *
     * This method is used to reload the BasementPlugin instance. It triggers a reload operation, which can involve
     * re-initializing certain components or refreshing resources used by the plugin. After the reload operation is completed,
     * the plugin is in its reloaded state and ready to continue its operation.
     *
     * @throws Exception if an error occurs during the reload operation.
     */
    void reload();

    /**
     * Updates the configuration
     * This method is used to update the configuration settings.
     */
    void update(Class<?> loader);

    /**
     * Gets the value of a string property specified by the given path.
     * If the property doesn't exist, it returns the default value provided.
     *
     * @param path The path of the string property.
     * @param def The default value to be returned if the property doesn't exist.
     * @return The value of the string property specified by the path, or the default value if property doesn't exist.
     */
    String getString(String path, String def);

    /**
     * Retrieves an integer value from the specified path in a configuration file.
     * If the path does not exist or the retrieved value is not an integer, the default value will be returned.
     *
     * @param path The path to the integer value in the configuration file.
     * @param def The default value to be returned if the path does not exist or the retrieved value is not an integer.
     * @return The integer value at the specified path, or the default value if the path does not exist or the retrieved value is not an integer.
     */
    int getInteger(String path, int def);

    /**
     * Returns the double value associated with the given path in the configuration file
     * or the default value if no value is found.
     *
     * @param path the path to retrieve the double value from
     * @param def the default value to return if the path does not exist or the value is not a valid double
     * @return the double value at the given path, or the default value if no value is found or the value is not a valid double
     */
    double getDouble(String path, double def);

    /**
     * Retrieves the float value from the specified path.
     *
     * @param path the path of the float value
     * @param def  the default float value to return if the path is not found or the value is invalid
     * @return the float value retrieved from the path, or the default value if not found or invalid
     */
    float getFloat(String path, float def);

    /**
     * Returns the value associated with the specified path as a long. If the path does not exist or the value is
     * not a valid long, the default value supplied will be returned.
     *
     * @param path the path to retrieve the value from
     * @param def the default value to return if the path does not exist or the value is not a valid long
     * @return the value associated with the specified path as a long, or the default value if the path does not exist
     *         or the value is not a valid long
     */
    long getLong(String path, long def);

    /**
     * Retrieves the boolean value associated with the given path. If the path
     * does not exist or cannot be parsed as a boolean value, the provided default
     * value is returned.
     *
     * @param path The path of the boolean value to retrieve.
     * @param def The default value to return if the path does not exist or cannot
     *            be parsed as a boolean value.
     * @return The boolean value associated with the given path, or the default
     *         value if the path does not exist or cannot be parsed.
     */
    boolean getBoolean(String path, boolean def);

    /**
     * Returns a List of Strings for the specified path, or the default value if the path does not exist or the value is not a List of Strings.
     *
     * @param path the path of the configuration key to retrieve the List of Strings from
     * @param def the default value to return if the path does not exist or the value is not a List of Strings
     * @return a List of Strings for the specified path, or the default value if the path does not exist or the value is not a List of Strings
     */
    List<String> getStringList(String path, List<String> def);

    /**
     * Retrieves the value at the given path as a string list.
     * NOTE: This method works only in bukkit.
     *
     * @param path the path to the value
     * @return a ConfigurationSection representing the value at the given path
     */
    Object section(String path);

    /**
     * Returns a Map of Strings that represent the content of the specified path.
     *
     * @param path The path to the file or resource.
     * @param def  The default Map to return if the path does not exist or cannot be read.
     * @return A Map of Strings representing the content of the specified path, or the default Map
     *         if the path does not exist or cannot be read.
     */
    Map<String, String> getStringMap(String path, Map<String, String> def);

    /**
     * Retrieves the value corresponding to the given path from the provided map.
     * If the value is not found, the default value is returned.
     *
     * @param path The path to the desired value in the map. The path is represented as a string separated by dots.
     * @param def The default value to return if the path is not found in the map.
     * @return The value corresponding to the path if found in the map, otherwise returns the default value.
     */
    Object get(String path, Object def);

    /**
     * Retrieves the value at the specified path from the data structure.
     *
     * @param path the path to the object in the data structure
     * @param def the default value to return if the specified path does not exist
     * @return the value at the specified path if it exists, otherwise returns the default value
     */
    Object set(String path, Object obj);


}
