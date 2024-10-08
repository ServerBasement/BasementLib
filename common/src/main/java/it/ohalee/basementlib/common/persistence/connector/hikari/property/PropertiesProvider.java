package it.ohalee.basementlib.common.persistence.connector.hikari.property;

import java.util.Properties;

public class PropertiesProvider {

    private final Properties properties = new Properties();

    public void withProperties(PropertyPair... pairs) {
        for (PropertyPair propertyPair : pairs) {
            properties.setProperty(propertyPair.getProperty().toString(), propertyPair.getValue());
        }
        properties.setProperty("useSSL", "false");
        properties.setProperty("verifyServerCertificate", "false");
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "utf8");
    }

    public Properties getBuild() {
        return properties;
    }

}
