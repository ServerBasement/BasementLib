package it.ohalee.basementlib.common.config;

import it.ohalee.basementlib.api.config.Configuration;
import it.ohalee.basementlib.api.config.property.Property;

import java.util.List;

public class BasementConfig extends Configuration {

    public static Property<String> MARIA_HOST;
    public static Property<String> MARIA_USERNAME;
    public static Property<String> MARIA_PASSWORD;

    public static Property<List<String>> REDIS_HOSTS;

}
