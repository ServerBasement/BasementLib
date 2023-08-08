package it.ohalee.basementlib.common.plugin.bootstrap;

import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import it.ohalee.basementlib.common.plugin.classpath.ClassPathAppender;

import java.nio.file.Path;

public interface BasementBootstrap {

    Path dataDirectory();

    PluginLogger logger();

    ClassPathAppender getClassPathAppender();
}
