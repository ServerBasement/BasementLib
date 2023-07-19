package it.ohalee.basementlib.api.persistence.sql.structure;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderAlterTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderDropTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderTableExists;

/**
 * Represents the abstract definition of the MariaDatabase class,
 * it contains the main functions that can be performed from the outside.
 */
public interface AbstractSqlDatabase extends AbstractQueryExecutor {

    /**
     * Name of the database
     *
     * @return name
     */
    String getName();

    QueryBuilderTableExists tableExists(String tableName);

    /**
     * initializes a query to alter a table on this database
     *
     * @param tableName name of the table to be altered
     * @return query to build
     */
    QueryBuilderAlterTable alterTable(String tableName);

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
    AbstractSqlTable useTable(String tableName);

}
