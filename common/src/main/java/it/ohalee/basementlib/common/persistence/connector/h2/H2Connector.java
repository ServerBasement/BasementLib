package it.ohalee.basementlib.common.persistence.connector.h2;

import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.common.persistence.connector.SqlConnector;
import org.h2.jdbc.JdbcConnection;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class H2Connector extends SqlConnector {

    private NonClosableConnection connection;
    private final Path path;

    public H2Connector(Path path) {
        super();
        this.path = path;
    }

    @Override
    public TypeConnector getType() {
        return TypeConnector.H2;
    }

    @Override
    public void connect(String host) {
        if (connection != null) {
            throw new RuntimeException("Already connected");
        }

        try {
            Path path = this.path.resolve("data/" + host);
            connection = new NonClosableConnection(new JdbcConnection("jdbc:h2:" + path.toFile().getAbsolutePath(), new Properties()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connect(String host, String username) {
        connect(host);
    }

    @Override
    public void connect(String host, String username, String password) {
        connect(host);
    }

    @Override
    public void close() {
        try {
            connection.shutdown();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return connection;
    }


}
