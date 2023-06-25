package it.ohalee.basementlib.common.persistence.connector;

import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.sql.structure.data.QueryData;
import it.ohalee.basementlib.common.persistence.sql.data.QueryDataImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class SqlConnector implements Connector {

    @Override
    public void connect(StorageCredentials credentials) {
        connect(credentials.address(), credentials.username(), credentials.password());
    }

    @Override
    public void execute(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException throwable) {
            Logger.getGlobal().severe("Error on sql query:" + query);
            throwable.printStackTrace();
        }
    }

    @Override
    public PreparedStatement asPrepared(String query) {
        try {
            return getConnection().prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    public QueryData executeReturn(String query) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            return new QueryDataImpl(statement.executeQuery());
        } catch (SQLException throwable) {
            Logger.getGlobal().severe("Error on sql query:" + query);
            throwable.printStackTrace();
        }
        return null;
    }

    protected abstract Connection getConnection() throws SQLException;

}
