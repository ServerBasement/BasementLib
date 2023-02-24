package it.ohalee.basementlib.api.persistence.maria.structure;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderDropTable;

/**
 * Represents the abstract definition of the MariaDatabase class,
 * it contains the main functions that can be performed from the outside.
 */
public interface AbstractMariaDatabase extends AbstractQueryExecutor {

    /**
     * Name of the database
     *
     * @return name
     */
    String getName();

    /**
     * initializes a query to create a table on this database
     *
     * @param tableName name of the table to be created
     * @return query to build
     */
    QueryBuilderCreateTable createTable(String tableName);

    /**
     * initializes a query to drop a table on this database
     *
     * @param tableName name of the table to be drop
     * @return query to build
     */
    QueryBuilderDropTable dropTable(String tableName);

    /**
     * returns the abstract definition of
     * a table created by the ::createTable() method
     *
     * @param tableName name of the table
     * @return abstract definition of the table
     */
    AbstractMariaTable useTable(String tableName);

}
