package it.ohalee.basementlib.common.persistence.connector.hikari.maria;

import com.zaxxer.hikari.HikariConfig;
import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.common.persistence.connector.hikari.HikariConnector;
import it.ohalee.basementlib.common.persistence.connector.hikari.property.HikariProperty;
import it.ohalee.basementlib.common.persistence.connector.hikari.property.PropertiesProvider;
import it.ohalee.basementlib.common.persistence.connector.hikari.property.PropertyPair;

public class MariaConnector extends HikariConnector {

    public MariaConnector(int minPoolSize, int maxPoolSize, String poolName) {
        super(minPoolSize, maxPoolSize, poolName, "org.mariadb.jdbc.Driver");
    }

    @Override
    public TypeConnector getType() {
        return TypeConnector.MARIADB;
    }

    @Override
    protected HikariConfig getConfig(int minPoolSize, int maxPoolSize, String poolName) {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minPoolSize);
        config.setPoolName(poolName);
        return config;
    }

    @Override
    protected PropertiesProvider getProperties() {
        PropertiesProvider holder = new PropertiesProvider();
        holder.withProperties(
                new PropertyPair(HikariProperty.cachePrepStmts, "true"),
                new PropertyPair(HikariProperty.alwaysSendSetIsolation, "false"),
                new PropertyPair(HikariProperty.cacheServerConfiguration, "true"),
                new PropertyPair(HikariProperty.elideSetAutoCommits, "true"),
                new PropertyPair(HikariProperty.maintainTimeStats, "false"),
                new PropertyPair(HikariProperty.useLocalSessionState, "true"),
                new PropertyPair(HikariProperty.useServerPrepStmts, "true"),
                new PropertyPair(HikariProperty.prepStmtCacheSize, "500"),
                new PropertyPair(HikariProperty.rewriteBatchedStatements, "true"),
                new PropertyPair(HikariProperty.prepStmtCacheSqlLimit, "2048"),
                new PropertyPair(HikariProperty.cacheCallableStmts, "true"),
                new PropertyPair(HikariProperty.cacheResultSetMetadata, "true"),
                new PropertyPair(HikariProperty.characterEncoding, "utf8"),
                new PropertyPair(HikariProperty.useUnicode, "true"),
                new PropertyPair(HikariProperty.zeroDateTimeBehavior, "CONVERT_TO_NULL")
        );
        return holder;
    }

    @Override
    public void close() {
        source.close();
    }

    @Override
    public void connect(String host) {
        super.connect("jdbc:mariadb://" + host);
    }

    @Override
    public void connect(String host, String username) {
        super.connect("jdbc:mariadb://" + host, username);
    }

    @Override
    public void connect(String host, String username, String password) {
        super.connect("jdbc:mariadb://" + host, username, password);
    }

}
