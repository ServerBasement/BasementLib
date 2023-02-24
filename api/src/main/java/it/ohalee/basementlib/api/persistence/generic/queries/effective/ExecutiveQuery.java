package it.ohalee.basementlib.api.persistence.generic.queries.effective;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;

/**
 * represents an object that constructs and executes
 * operations only towards the database,
 * without expecting a return value
 *
 * @param <T> The type of QueryBuilder used
 */
public interface ExecutiveQuery<T> {

    /**
     * constructs the query to a String
     *
     * @return the QueryBuilder
     */
    T build();

    PreparedStatement asPrepared();

    /**
     * executes the string query through a Connector
     *
     * @return the QueryBuilder
     */
    T exec();

    /**
     * Clone a QueryBuilder returning a new one
     */
    T patternClone();

    /**
     * executes the string query through a Connector in asynchronous
     *
     * @return the QueryBuilder
     */
    CompletableFuture<T> execAsync();

    String getSql();
}
