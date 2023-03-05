package it.ohalee.basementlib.api.persistence.maria.structure;


import it.ohalee.basementlib.api.persistence.generic.Holder;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderDropDatabase;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * it represents the main access to the management of mariadb,
 * it contains the connections and the main operations that can be performed.
 */
public abstract class AbstractMariaHolder extends Holder implements AbstractQueryHolderExecutor {

    public AbstractMariaHolder(Connector connector) {
        super(connector);
    }

    public abstract void loadDatabase(AbstractMariaDatabase database);

    public abstract void unloadDatabase(AbstractMariaDatabase database);

    public abstract void unloadDatabase(String databaseName);

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

    /**
     * returns the abstract definition of
     * a database created by the ::createDatabase() method
     *
     * @param databaseName name of the database
     * @return abstract definition of the database
     */
    public abstract @Nullable AbstractMariaDatabase useDatabase(String databaseName);
}
