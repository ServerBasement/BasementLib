package it.ohalee.basementlib.common.persistence.maria.structure;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.*;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderDropDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.common.persistence.maria.queries.data.*;
import it.ohalee.basementlib.common.persistence.maria.queries.database.QueryCreateDatabase;
import it.ohalee.basementlib.common.persistence.maria.queries.database.QueryDropDatabase;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MariaHolder extends AbstractMariaHolder {

    private final Map<String, AbstractMariaDatabase> databases = new HashMap<>();

    public MariaHolder(Connector connector) {
        super(connector);
    }

    public void loadDatabase(AbstractMariaDatabase database) {
        if (databases.containsKey(database.getName()))
            throw new IllegalArgumentException("Database " + database.getName() + " already loaded!");
        databases.put(database.getName(), database);
    }

    public void unloadDatabase(AbstractMariaDatabase database) {
        unloadDatabase(database.getName());
    }

    public void unloadDatabase(String databaseName) {
        databases.remove(databaseName);
    }

    /*
        Super use
     */

    @Override
    public @Nullable AbstractMariaDatabase useDatabase(String databaseName) {
        return databases.get(databaseName);
    }

    @Override
    public QueryBuilderCreateDatabase createDatabase(String databaseName) {
        return new QueryCreateDatabase(this, databaseName);
    }

    @Override
    public QueryBuilderDropDatabase dropDatabase(String databaseName) {
        return new QueryDropDatabase(this, databaseName);
    }

    /*
        Query Executor
     */

    @Override
    public QueryBuilderSelect select(String database) {
        return new QuerySelect(this, database);
    }

    @Override
    public QueryBuilderInsert insert(String database) {
        return new QueryInsert(this, database);
    }

    @Override
    public QueryBuilderDelete delete(String database) {
        return new QueryDelete(this, database);
    }

    @Override
    public QueryBuilderUpdate update(String database) {
        return new QueryUpdate(this, database);
    }

    @Override
    public QueryBuilderReplace replace(String database) {
        return new QueryReplace(this, database);
    }

}
