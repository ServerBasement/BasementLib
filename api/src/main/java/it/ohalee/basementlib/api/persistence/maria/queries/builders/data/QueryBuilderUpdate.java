package it.ohalee.basementlib.api.persistence.maria.queries.builders.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;
import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;

/**
 * Builder representing an 'UPDATE' Query
 */
public interface QueryBuilderUpdate extends ExecutiveQuery<QueryBuilderUpdate> {

    /**
     * Specifies the table to be used in the query
     *
     * @param tableName table's name
     * @return self Query Builder
     */
    QueryBuilderUpdate table(String... tableName);

    /**
     * Specifies the value to be set on a column.
     * The method could be reused to set multiple columns.
     *
     * @param column the column took in action
     * @param value  the value to be set on this column
     * @return self Query Builder
     */
    QueryBuilderUpdate set(String column, Object value);

    /**
     * Same as QueryBuilderUpdate::set(), made for consistency,
     * but value would not be quoted.
     *
     * @param column the column took in action
     * @param value  the value to be set on this column
     * @return self Query Builder
     */
    QueryBuilderUpdate setNQ(String column, Object value);

    /**
     * Specifies the value to be set on a column.
     * The method could be reused to set multiple columns.
     *
     * @param column the column took in action
     * @param value  the value to be set on this column
     * @return self Query Builder
     */
    QueryBuilderUpdate set(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value);

    /**
     * Specifies the value to be added on a column.
     * The method could be reused to set multiple columns.
     *
     * @param column the column took in action
     * @param value  the value to be set on this column
     * @return self Query Builder
     */
    QueryBuilderUpdate add(String column, Number value);

    /**
     * Specifies the value to be subtracted on a column.
     * The method could be reused to set multiple columns.
     *
     * @param column the column took in action
     * @param value  the value to be set on this column
     * @return self Query Builder
     */
    QueryBuilderUpdate subtract(String column, Number value);

    /**
     * Specifies the full condition
     * applied to the query
     *
     * @param conditions full condition
     * @return self Query Builder
     */
    QueryBuilderUpdate where(String conditions);

    /**
     * Specifies the full ORDER BY
     * block statement
     *
     * @param statement group by expression
     * @return self Query Builder
     */
    QueryBuilderUpdate orderBy(String statement);

    /**
     * Specifies the LIMIT block
     * applied to the query
     *
     * @param n limit
     * @return self Query Builder
     */
    QueryBuilderUpdate limit(int n);

    QueryBuilderUpdate clearSet();
}
