package it.ohalee.basementlib.api.config.generic.adapter;

import it.ohalee.basementlib.api.plugin.BasementPlugin;

import java.util.List;
import java.util.Map;

public interface ConfigurationAdapter {

    BasementPlugin getPlugin();

    void reload();

    String getString(String path, String def);

    int getInteger(String path, int def);

    boolean getBoolean(String path, boolean def);

    List<String> getStringList(String path, List<String> def);

    Map<String, String> getStringMap(String path, Map<String, String> def);

    Object get(String path, Object def);

    Object set(String path, Object obj);


}
