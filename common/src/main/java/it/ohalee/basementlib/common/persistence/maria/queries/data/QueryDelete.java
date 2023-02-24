package it.ohalee.basementlib.common.persistence.maria.queries.data;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.QueryBuilderDelete;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.persistence.maria.structure.data.QueryData;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QueryDelete extends MariaQuery implements QueryBuilderDelete {

    private String tableName;
    private String multi;
    private String where;
    private String orderBy;
    private String returning;
    private int limit;

    public QueryDelete() {
    }

    public QueryDelete(AbstractMariaHolder holder, String database) {
        super(holder, database);
    }

    @Override
    public QueryBuilderDelete from(String tableName) {
        this.tableName = databaseName + "." + tableName;
        return this;
    }

    @Override
    public QueryBuilderDelete multiFrom(String... from) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String table : from) {
            if (first) {
                first = false;
                builder.append(databaseName).append(".").append(table);
                continue;
            }
            builder.append(", ").append(databaseName).append(".").append(table);
        }
        tableName = builder.toString();
        return this;
    }

    @Override
    public QueryBuilderDelete where(String conditions) {
        where = conditions;
        return this;
    }

    @Override
    public QueryBuilderDelete orderBy(String statement) {
        orderBy = statement;
        return this;
    }

    @Override
    public QueryBuilderDelete limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public QueryBuilderDelete returning(String expression) {
        this.returning = expression;
        return this;
    }

    @Override
    public QueryBuilderDelete multiTable(String... selector) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String table : selector) {
            if (first) {
                first = false;
                builder.append(databaseName).append(".").append(table);
                continue;
            }
            builder.append(", ").append(databaseName).append(".").append(table);
        }
        multi = builder.toString();
        return this;
    }

    @Override
    public QueryBuilderDelete build() {
        StringBuilder builder = new StringBuilder("DELETE ");
        if (multi == null) {
            builder.append("FROM ").append(tableName);
        } else {
            builder.append(multi).append(" FROM ").append(tableName);
        }
        if (where != null)
            builder.append(" WHERE ").append(where);
        if (orderBy != null)
            builder.append(" ORDER BY ").append(orderBy);
        if (limit != 0)
            builder.append(" LIMIT ").append(limit);
        if (returning != null)
            builder.append(" RETURNING ").append(returning);
        setSql(builder.append(";").toString());
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderDelete exec() {
        getConnector().execute(getSql());
        return this;
    }

    @Override
    public QueryBuilderDelete patternClone() {
        QueryDelete copy = new QueryDelete(holder, databaseName);
        copy.limit = limit;
        copy.orderBy = orderBy;
        copy.tableName = tableName;
        copy.multi = multi;
        copy.where = where;
        copy.returning = returning;
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderDelete> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

    @Override
    public QueryData execReturn() {
        return getConnector().executeReturn(getSql());
    }

    @Override
    public CompletableFuture<QueryData> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }

    @Override
    public QueryBuilderDelete execConsume(Consumer<QueryData> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderDelete> execConsumeAsync(Consumer<QueryData> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public QueryData execReturnAfter(UnaryOperator<QueryData> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<QueryData> execReturnAfterAsync(UnaryOperator<QueryData> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }
}
