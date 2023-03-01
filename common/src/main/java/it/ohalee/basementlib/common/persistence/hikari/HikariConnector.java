package it.ohalee.basementlib.common.persistence.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.common.persistence.hikari.property.PropertiesProvider;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnector implements Connector {

    protected final HikariConfig config;
    protected HikariDataSource source;

    public HikariConnector(int minPoolSize, int maxPoolSize, String poolName, String driver) {
        PropertiesProvider provider = getProperties();
        if (provider == null) {
            provider = new PropertiesProvider();
        }

        config = getConfig(minPoolSize, maxPoolSize, poolName);
        config.setDriverClassName(driver);
        config.setDataSourceProperties(provider.getBuild());
    }

    @Override
    public void connect(StorageCredentials credentials) {
        connect(credentials.address(), credentials.username(), credentials.password());
    }

    @Override
    public void connect(String host) {
        config.setJdbcUrl(host);
        source = new HikariDataSource(config);
        establish();
    }

    @Override
    public void connect(String host, String username) {
        config.setJdbcUrl(host);
        config.setUsername(username);
        source = new HikariDataSource(config);
        establish();
    }

    @Override
    public void connect(String host, String username, String password) {
        config.setJdbcUrl(host);
        config.setUsername(username);
        config.setPassword(password);
        source = new HikariDataSource(config);
        establish();
    }

    private void establish() {
        try (Connection connection = source.getConnection()) {
            System.out.println("Connection Established... " + connection);
        } catch (SQLException throwable) {
            System.out.println("Unable to Establish Connection...");
            throwable.printStackTrace();
        }
    }

    protected abstract HikariConfig getConfig(int minPoolSize, int maxPoolSize, String poolName);

    protected abstract PropertiesProvider getProperties();

}
