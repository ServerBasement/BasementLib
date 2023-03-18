/*
 *
 * This file is part of servercore - servercore.common.main | ConfigKeys.java
 *
 *  Copyright (c) ohAlee (Ale) <alebartoh@gmail.com>
 *  Copyright (c) 2021-2022.
 *
 *  You can't use this code without the owner permission.
 */

package it.ohalee.basementlib.common.config;

import it.ohalee.basementlib.api.config.generic.KeyedConfiguration;
import it.ohalee.basementlib.api.config.generic.key.ConfigKey;
import it.ohalee.basementlib.api.config.generic.key.SimpleConfigKey;
import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.redis.RedisCredentials;

import java.util.Collections;
import java.util.List;

import static it.ohalee.basementlib.api.config.generic.key.ConfigKeyFactory.*;

public final class ConfigKeys {

    public static final ConfigKey<String> SERVER = lowercaseStringKey("server", "unknown");

    public static final ConfigKey<StorageCredentials> MYSQL_CREDENTIALS = notReloadable(key(c -> {
        int maxPoolSize = c.getInteger("mysql.pool-settings.maximum-pool-size", c.getInteger("mysql.pool-size", 10));
        int minIdle = c.getInteger("mysql.pool-settings.minimum-idle", maxPoolSize);
        int maxLifetime = c.getInteger("mysql.pool-settings.maximum-lifetime", 1800000);
        int keepAliveTime = c.getInteger("mysql.pool-settings.keepalive-time", 0);
        int connectionTimeout = c.getInteger("mysql.pool-settings.connection-timeout", 5000);
        return new StorageCredentials(
                c.getBoolean("mysql.enabled", true),
                c.getString("mysql.address", null),
                c.getString("mysql.database", null),
                c.getString("mysql.username", null),
                c.getString("mysql.password", null),
                maxPoolSize, minIdle, maxLifetime, keepAliveTime, connectionTimeout
        );
    }));

    public static final ConfigKey<RedisCredentials> REDIS_CREDENTIALS = notReloadable(key(c -> {
        return new RedisCredentials(
                c.getBoolean("redis.enabled", true),
                c.getStringList("redis.addresses", Collections.emptyList()),
                c.getString("redis.username", null),
                c.getString("redis.password", null),
                c.getInteger("redis.netty-threads", 32),
                c.getInteger("redis.threads", 16)
        );
    }));

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(ConfigKeys.class);

    private ConfigKeys() {
    }

    public static ConfigKey<Integer> getInteger(String path) {
        return key(c -> c.getInteger(path, 0));
    }

    public static ConfigKey<String> getString(String path) {
        return key(c -> c.getString(path, null));
    }

    public static ConfigKey<List<String>> getStringList(String path) {
        return key(c -> c.getStringList(path, null));
    }

    public static ConfigKey<Boolean> getBoolean(String path) {
        return key(c -> c.getBoolean(path, false));
    }

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }

}
