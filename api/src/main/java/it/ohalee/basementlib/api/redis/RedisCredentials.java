package it.ohalee.basementlib.api.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public final class RedisCredentials {

    private final List<String> hosts;
}
