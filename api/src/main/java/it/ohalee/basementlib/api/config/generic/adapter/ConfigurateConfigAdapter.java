package it.ohalee.basementlib.api.config.generic.adapter;

import it.ohalee.basementlib.api.plugin.BasementPlugin;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ConfigurateConfigAdapter implements ConfigurationAdapter {
    private final BasementPlugin plugin;
    private final Path path;
    private ConfigurationNode root;

    public ConfigurateConfigAdapter(BasementPlugin plugin, Path path) {
        this.plugin = plugin;
        this.path = path;
        reload();
    }

    protected abstract ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path);

    @Override
    public void reload() {
        ConfigurationLoader<? extends ConfigurationNode> loader = createLoader(this.path);
        try {
            this.root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ConfigurationNode node) {
        ConfigurationLoader<? extends ConfigurationNode> loader = createLoader(this.path);
        try {
            loader.save(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConfigurationNode resolvePath(String path) {
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }
        return this.root.getNode(Arrays.stream(path.split("\\.")).toArray());
    }

    @Override
    public String getString(String path, String def) {
        return resolvePath(path).getString(def);
    }

    @Override
    public int getInteger(String path, int def) {
        return resolvePath(path).getInt(def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return resolvePath(path).getBoolean(def);
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        ConfigurationNode node = resolvePath(path);
        if (node.isVirtual() || !node.isList()) {
            return def;
        }

        return node.getList(Object::toString);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getStringMap(String path, Map<String, String> def) {
        ConfigurationNode node = resolvePath(path);
        if (node.isVirtual()) {
            return def;
        }

        Map<String, Object> m = (Map<String, Object>) node.getValue(Collections.emptyMap());
        return m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().toString()));
    }

    @Override
    public Object get(String path, Object def) {
        ConfigurationNode node = resolvePath(path);
        return node.getValue(def);
    }

    @Override
    public Object set(String path, Object obj) {
        ConfigurationNode node = resolvePath(path);
        node.setValue(obj);
        save(node);
        reload();
        return obj;
    }

    @Override
    public BasementPlugin getPlugin() {
        return this.plugin;
    }
}