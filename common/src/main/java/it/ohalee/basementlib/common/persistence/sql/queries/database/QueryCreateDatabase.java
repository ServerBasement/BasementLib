package it.ohalee.basementlib.common.persistence.sql.queries.database;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.sql.queries.effective.SqlQuery;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import it.ohalee.basementlib.common.persistence.sql.SqlDatabase;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QueryCreateDatabase extends SqlQuery implements QueryBuilderCreateDatabase {

    private boolean orReplace = false;
    private boolean ifNotExists = false;
    private SqlDatabase temp;

    public QueryCreateDatabase(AbstractSqlHolder holder, String databaseName) {
        super(holder, databaseName);
    }

    @Override
    public QueryBuilderCreateDatabase orReplace(boolean add) {
        orReplace = add;
        return this;
    }

    @Override
    public QueryBuilderCreateDatabase ifNotExists(boolean add) {
        ifNotExists = add;
        return this;
    }

    @Override
    public QueryBuilderCreateDatabase build() {
        StringBuilder builder = new StringBuilder("CREATE ");
        if (orReplace)
            builder.append("OR REPLACE ");
        builder.append("DATABASE ");
        if (ifNotExists)
            builder.append("IF NOT EXISTS ");
        builder.append(databaseName).append(";");

        setSql(builder.toString());
        temp = new SqlDatabase(holder, databaseName);
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderCreateDatabase exec() {
        getConnector().execute(getSql());
        holder.loadDatabase(temp);
        return this;
    }

    @Override
    public QueryBuilderCreateDatabase patternClone() {
        QueryCreateDatabase copy = new QueryCreateDatabase(holder, databaseName);
        copy.ifNotExists = ifNotExists;
        copy.orReplace = orReplace;
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateDatabase> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }


    @Override
    public AbstractSqlDatabase execReturn() {
        exec();
        return temp;
    }

    @Override
    public CompletableFuture<AbstractSqlDatabase> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }


    @Override
    public QueryBuilderCreateDatabase execConsume(Consumer<AbstractSqlDatabase> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateDatabase> execConsumeAsync(Consumer<AbstractSqlDatabase> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public AbstractSqlDatabase execReturnAfter(UnaryOperator<AbstractSqlDatabase> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<AbstractSqlDatabase> execReturnAfterAsync(UnaryOperator<AbstractSqlDatabase> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
