package it.ohalee.basementlib.common.persistence.sql;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.*;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderDropDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import it.ohalee.basementlib.common.persistence.sql.queries.data.*;
import it.ohalee.basementlib.common.persistence.sql.queries.database.QueryCreateDatabase;
import it.ohalee.basementlib.common.persistence.sql.queries.database.QueryDropDatabase;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SqlHolder extends AbstractSqlHolder {

    private final Map<String, AbstractSqlDatabase> databases = new HashMap<>();
    private final boolean h2;

    public SqlHolder(Connector connector, boolean h2) {
        super(connector);
        this.h2 = h2;
    }

    public void loadDatabase(AbstractSqlDatabase database) {
        if (databases.containsKey(database.getName()))
            throw new IllegalArgumentException("Database " + database.getName() + " already loaded!");
        databases.put(database.getName(), database);
    }

    public void unloadDatabase(AbstractSqlDatabase database) {
        unloadDatabase(database.getName());
    }

    public void unloadDatabase(String databaseName) {
        databases.remove(databaseName);
    }

    /*
        Super use
     */

    @Override
    public @Nullable AbstractSqlDatabase useDatabase(String databaseName) {
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

    @Override
    public boolean isH2() {
        return h2;
    }
}
