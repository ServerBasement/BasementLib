package it.ohalee.basementlib.api.persistence.sql.structure;


import it.ohalee.basementlib.api.persistence.generic.Holder;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderDropDatabase;

/**
 * it represents the main access to the management of mariadb,
 * it contains the connections and the main operations that can be performed.
 */
public abstract class AbstractSqlHolder extends Holder implements AbstractQueryHolderExecutor, LocalFactory {

    public AbstractSqlHolder(Connector connector) {
        super(connector);
    }

    /**
     * initialize a query to create a database
     * and register it on this holder
     *
     * @param databaseName name of the database to be created
     * @return the query to build
     */
    public abstract QueryBuilderCreateDatabase createDatabase(String databaseName);

    /**
     * initialize a query to drop a database
     * registered on this holder
     *
     * @param databaseName name of the database to be drop
     * @return
     */
    public abstract QueryBuilderDropDatabase dropDatabase(String databaseName);

    public abstract boolean isH2();
}
