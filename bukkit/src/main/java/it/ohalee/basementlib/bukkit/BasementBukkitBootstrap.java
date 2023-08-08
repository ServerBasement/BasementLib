package it.ohalee.basementlib.bukkit;

import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.common.dependencies.Dependency;
import it.ohalee.basementlib.common.dependencies.DependencyManager;
import it.ohalee.basementlib.common.loader.LoaderBootstrap;
import it.ohalee.basementlib.common.plugin.bootstrap.BasementBootstrap;
import it.ohalee.basementlib.common.plugin.classpath.ClassPathAppender;
import it.ohalee.basementlib.common.plugin.classpath.JarInJarClassPathAppender;
import it.ohalee.basementlib.common.plugin.logging.JavaPluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

public class BasementBukkitBootstrap implements BasementBootstrap, LoaderBootstrap {

    private final JavaPlugin plugin;
    private final ClassPathAppender classPathAppender;
    private DependencyManager dependencyManager;
    private BasementBukkitPlugin basement;

    public BasementBukkitBootstrap(JavaPlugin plugin) {
        this.plugin = plugin;
        this.classPathAppender = new JarInJarClassPathAppender(getClass().getClassLoader());
    }

    @Override
    public void onLoad() {
        // load dependencies
        this.dependencyManager = new DependencyManager(this);
        this.dependencyManager.loadDependencies(getGlobalDependencies());

        this.basement = new BasementBukkitPlugin(plugin);
    }

    @Override
    public void onEnable() {
        this.basement.enable();
    }

    @Override
    public void onDisable() {
        this.basement.disable();
    }

    protected Set<Dependency> getGlobalDependencies() {
        return EnumSet.of(
                Dependency.CAFFEINE,
                Dependency.BYTEBUDDY,
                Dependency.MARIADB_DRIVER,
                Dependency.MYSQL_DRIVER,
                Dependency.H2_DRIVER_LEGACY,
                Dependency.HIKARI,
                Dependency.CONFIGURATE_CORE,
                Dependency.CONFIGURATE_YAML,
                Dependency.REDISSON
        );
    }

    public DependencyManager getDependencyManager() {
        return this.dependencyManager;
    }

    @Override
    public Path dataDirectory() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public PluginLogger logger() {
        return new JavaPluginLogger(this.plugin.getLogger());
    }

    @Override
    public ClassPathAppender getClassPathAppender() {
        return this.classPathAppender;
    }

}
