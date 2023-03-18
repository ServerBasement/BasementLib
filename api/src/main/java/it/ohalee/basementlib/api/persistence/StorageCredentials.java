package it.ohalee.basementlib.api.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class StorageCredentials {

    private final boolean enabled;
    private final String address;
    private final String database;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final int minIdleConnections;
    private final int maxLifetime;
    private final int keepAliveTime;
    private final int connectionTimeout;

}
