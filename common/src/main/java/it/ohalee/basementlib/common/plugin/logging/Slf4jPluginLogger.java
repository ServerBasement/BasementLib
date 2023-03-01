/*
 *
 * This file is part of servercore - servercore.common.main | Slf4jPluginLogger.java
 *
 *  Copyright (c) ohAlee (Ale) <alebartoh@gmail.com>
 *  Copyright (c) 2022-2022.
 *
 *  You can't use this code without the owner permission.
 */

package it.ohalee.basementlib.common.plugin.logging;

import it.ohalee.basementlib.api.plugin.logging.PluginLogger;
import org.slf4j.Logger;

public class Slf4jPluginLogger implements PluginLogger {
    private final Logger logger;

    public Slf4jPluginLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void warn(String s) {
        this.logger.warn(s);
    }

    @Override
    public void warn(String s, Throwable t) {
        this.logger.warn(s, t);
    }

    @Override
    public void severe(String s) {
        this.logger.error(s);
    }

    @Override
    public void severe(String s, Throwable t) {
        this.logger.error(s, t);
    }
}