package it.ohalee.basementlib.velocity;

import it.ohalee.basementlib.common.plugin.classpath.ClassPathAppender;

import java.nio.file.Path;

public class VelocityClassPathAppender implements ClassPathAppender {
    private final BasementVelocity bootstrap;

    public VelocityClassPathAppender(BasementVelocity bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public void addJarToClasspath(Path file) {
        this.bootstrap.getServer().getPluginManager().addToClasspath(this.bootstrap, file);
    }
}