package it.ohalee.basementlib.api.persistence.maria.queries.builders.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.data.QueryData;

/**
 * Builder representing a 'SELECT' Query
 */
public interface QueryBuilderSelect extends ReturningQuery<QueryBuilderSelect, QueryData> {

    /**
     * Specifies the columns to select from a table
     *
     * @param columns each column to concat in the final expression
     * @return self Query Builder
     */
    QueryBuilderSelect columns(String... columns);

    /**
     * Specifies already a final expression to be used
     * in the final query
     *
     * @param statement full final expression
     * @return self Query Builder
     */
    QueryBuilderSelect columns(String statement);

    /**
     * Specifies the tables used in the query
     * from the same database
     *
     * @param tables table's name
     * @return self Query Builder
     */
    QueryBuilderSelect from(String... tables);

    /**
     * Specifies the tables used in the query
     * from another database
     *
     * @param fullyTableName table's name qualified with database (database.table)
     * @return self Query Builder
     */
    QueryBuilderSelect fromExternal(String... fullyTableName);

    /**
     * Specifies the full condition
     * applied to the query
     *
     * @param conditions full condition
     * @return self Query Builder
     */
    QueryBuilderSelect where(String conditions);

    /**
     * Specifies the full GROUP BY
     * block statement
     *
     * @param statement order by expression
     * @return self Query Builder
     */
    QueryBuilderSelect groupBy(String statement);

    /**
     * Specifies the full HAVING
     * block statement
     *
     * @param conditions full having condition
     * @return self Query Builder
     */
    QueryBuilderSelect having(String conditions);

    /**
     * Specifies the full ORDER BY
     * block statement
     *
     * @param statement group by expression
     * @return self Query Builder
     */
    QueryBuilderSelect orderBy(String statement);

    /**
     * Specifies the LIMIT block
     * applied to the query
     *
     * @param limit  limiting by
     * @param offset first offset
     * @return self Query Builder
     */
    QueryBuilderSelect limit(int limit, int offset);
}
