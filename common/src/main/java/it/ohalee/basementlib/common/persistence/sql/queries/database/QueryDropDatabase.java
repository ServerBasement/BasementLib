package it.ohalee.basementlib.common.persistence.sql.queries.database;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderDropDatabase;
import it.ohalee.basementlib.api.persistence.sql.queries.effective.SqlQuery;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;

public class QueryDropDatabase extends SqlQuery implements QueryBuilderDropDatabase {

    private boolean ifExists = false;

    public QueryDropDatabase(AbstractSqlHolder holder, String databaseName) {
        super(holder, databaseName);
    }

    @Override
    public QueryBuilderDropDatabase ifExists(boolean add) {
        ifExists = add;
        return this;
    }

    @Override
    public QueryBuilderDropDatabase build() {
        StringBuilder builder = new StringBuilder("DROP DATABASE ");
        if (ifExists)
            builder.append("IF EXISTS ");
        builder.append(databaseName).append(";");
        setSql(builder.toString());
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderDropDatabase exec() {
        getConnector().execute(getSql());
        holder.unloadDatabase(databaseName);
        return this;
    }

    @Override
    public QueryBuilderDropDatabase patternClone() {
        QueryDropDatabase copy = new QueryDropDatabase(holder, databaseName);
        copy.ifExists = ifExists;
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderDropDatabase> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
