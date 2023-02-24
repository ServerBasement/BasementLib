package it.ohalee.basementlib.api.persistence.generic.queries.effective;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * @param <T> The QueryBuilder type
 * @param <K> Type of the expected return value
 */
public interface ReturningQuery<T, K> extends ExecutiveQuery<T> {

    /**
     * executes the string query through a Connector
     * returning the expected object
     *
     * @return The expected object
     */
    K execReturn();

    /**
     * executes the string query through a Connector
     * in async returning the expected object
     *
     * @return The expected object
     */
    CompletableFuture<K> execReturnAsync();

    /**
     * consumes the object expected by the consumer
     * passed by parameter and returns the QueryBuilder used
     *
     * @param digest
     * @return The QueryBuilder object
     */
    T execConsume(Consumer<K> digest);

    /**
     * consumes the object expected by the consumer in async
     * passed by parameter and returns the QueryBuilder used
     *
     * @param digest
     * @return The QueryBuilder object
     */
    CompletableFuture<T> execConsumeAsync(Consumer<K> digest);

    /**
     * it mutate the expected object through
     * the UnaryOperator passed as parameter and returns
     * the new form of the same qualitative object.
     *
     * @param action
     * @return The expected object
     */
    K execReturnAfter(UnaryOperator<K> action);

    /**
     * it mutate the expected object through
     * the UnaryOperator in async passed as parameter and
     * returns the new form of the same qualitative object.
     *
     * @param action
     * @return The expected object
     */
    CompletableFuture<K> execReturnAfterAsync(UnaryOperator<K> action);

}
