package it.ohalee.basementlib.common.persistence.hikari.property;

import java.util.Properties;

public class PropertiesProvider {

    private final Properties properties = new Properties();

    public void withProperties(PropertyPair... pairs) {
        for (PropertyPair propertyPair : pairs) {
            properties.setProperty(propertyPair.getProperty().toString(), propertyPair.getValue());
        }
    }

    public Properties getBuild() {
        return properties;
    }

}
