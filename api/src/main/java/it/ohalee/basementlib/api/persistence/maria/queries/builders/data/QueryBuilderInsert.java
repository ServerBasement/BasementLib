package it.ohalee.basementlib.api.persistence.maria.queries.builders.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;

/**
 * Builder representing an 'INSERT' Query
 */
public interface QueryBuilderInsert extends ExecutiveQuery<QueryBuilderInsert> {

    /**
     * Specifies the table used in the query
     * INSERT .. INTO {tableName} ..
     *
     * @param tableName table's name
     * @return self Query Builder
     */
    QueryBuilderInsert into(String tableName);

    /**
     * if true it includes
     * IGNORE
     * block in the final query
     *
     * @param add if the block must be present
     * @return self Query Builder
     */
    QueryBuilderInsert ignore(boolean add);

    /**
     * Specifies the columns schema to be used
     * in the query
     * INSERT .. INTO table({SCHEMA}) ..
     *
     * @param columns name of each column to concat in the final schema
     * @return self Query Builder
     */
    QueryBuilderInsert columnSchema(String... columns);

    /**
     * Specifies one group of values to insert
     * into the table.
     * It can be reused to add multiple group of values.
     * INSERT .. VALUES({VALUE}[,{VALUE}...])
     *
     * @param values each value matching the schema
     * @return self Query Builder
     */
    QueryBuilderInsert values(Object... values);

    /**
     * As values()... but values are not quoted
     *
     * @param values each value matching the schema
     * @return self Query Builder
     */
    QueryBuilderInsert valuesNQ(Object... values);

}
