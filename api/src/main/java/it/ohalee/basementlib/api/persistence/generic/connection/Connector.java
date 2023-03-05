package it.ohalee.basementlib.api.persistence.generic.connection;

import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.maria.structure.data.QueryData;

import java.sql.PreparedStatement;

/**
 * It represents the abstraction of the object
 * that contains the real connections and configurations
 * to maria database.
 */
public interface Connector {

    /**
     * Try to connect to maria
     * database using the credentials
     *
     * @param credentials the credentials
     */
    void connect(StorageCredentials credentials);

    void connect(String host);

    /**
     * @param host     connection url
     * @param username username used for the authentication
     *                 TYPE LOGIN : USING PASSWORD NO
     */
    void connect(String host, String username);

    /**
     * @param host     connection url
     * @param username username used for the authentication
     * @param password password used for the authentication
     *                 TYPE LOGIN : USING PASSWORD YES
     */
    void connect(String host, String username, String password);

    /**
     * Executes a query
     *
     * @param query the query
     */
    void execute(String query);

    /**
     * Get prepared statement for batching operations<br>
     * WARNING: the connection must be closed:
     *
     * @param query the query statement batched
     */
    PreparedStatement asPrepared(String query);

    /**
     * executes a query
     * expecting a return value
     *
     * @param query the query
     * @return the return set of the query
     */
    QueryData executeReturn(String query);

    void close();

}
