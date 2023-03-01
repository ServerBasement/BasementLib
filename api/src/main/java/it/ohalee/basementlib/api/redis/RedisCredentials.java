package it.ohalee.basementlib.api.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public final class RedisCredentials {

    private final boolean enabled;
    private final List<String> hosts;
    private final String username;
    private final String password;
    private final int nettyThreads;
    private final int threads;

}
