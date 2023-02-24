package it.ohalee.basementlib.api.persistence.maria.structure;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.*;

/**
 * represents the interface containing the
 * main operations addressed to the tables,
 * in this case without directly specifying
 * the database name
 */
public interface AbstractQueryExecutor {
    /**
     * initializes a select query from the table
     *
     * @return query to build
     */
    QueryBuilderSelect select();

    /**
     * initializes an insert query in the table
     *
     * @return query to build
     */
    QueryBuilderInsert insert();

    /**
     * initializes a remove query from the table
     *
     * @return query to build
     */
    QueryBuilderDelete delete();

    /**
     * initializes an update query on a table
     *
     * @return query to build
     */
    QueryBuilderUpdate update();

    /**
     * initializes a replace query on a table
     *
     * @return query to build
     */
    QueryBuilderReplace replace();
}
