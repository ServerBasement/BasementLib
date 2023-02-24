package it.ohalee.basementlib.common.persistence.maria.queries.database;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.database.QueryBuilderCreateDatabase;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.common.persistence.maria.structure.MariaDatabase;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QueryCreateDatabase extends MariaQuery implements QueryBuilderCreateDatabase {

    private boolean orReplace = false;
    private boolean ifNotExists = false;
    private MariaDatabase temp;

    public QueryCreateDatabase(AbstractMariaHolder holder, String databaseName) {
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
        temp = new MariaDatabase(holder, databaseName);
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
    public AbstractMariaDatabase execReturn() {
        exec();
        return temp;
    }

    @Override
    public CompletableFuture<AbstractMariaDatabase> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }


    @Override
    public QueryBuilderCreateDatabase execConsume(Consumer<AbstractMariaDatabase> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateDatabase> execConsumeAsync(Consumer<AbstractMariaDatabase> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public AbstractMariaDatabase execReturnAfter(UnaryOperator<AbstractMariaDatabase> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<AbstractMariaDatabase> execReturnAfterAsync(UnaryOperator<AbstractMariaDatabase> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
