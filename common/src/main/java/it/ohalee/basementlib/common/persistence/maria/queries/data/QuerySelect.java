package it.ohalee.basementlib.common.persistence.maria.queries.data;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.QueryBuilderSelect;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.persistence.maria.structure.data.QueryData;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QuerySelect extends MariaQuery implements QueryBuilderSelect {

    private String columns;
    private String from;

    /*
        Building
     */
    private String fromExternal;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public QuerySelect() {
    }

    public QuerySelect(AbstractMariaHolder holder, String database) {
        super(holder, database);
    }

    @Override
    public QueryBuilderSelect columns(String... columns) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String column : columns) {
            if (first) {
                first = false;
                builder.append(column);
                continue;
            }
            builder.append(", ").append(column);
        }
        this.columns = builder.toString();
        return this;
    }

    @Override
    public QueryBuilderSelect columns(String statement) {
        columns = statement;
        return this;
    }

    @Override
    public QueryBuilderSelect from(String... tables) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String table : tables) {
            if (first) {
                first = false;
                builder.append(databaseName).append(".").append(table);
                continue;
            }
            builder.append(", ").append(databaseName).append(".").append(table);
        }
        this.from = builder.toString();
        return this;
    }

    @Override
    public QueryBuilderSelect fromExternal(String... fullyTableName) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String table : fullyTableName) {
            if (first) {
                first = false;
                builder.append(table);
                continue;
            }
            builder.append(", ").append(table);
        }
        this.fromExternal = builder.toString();
        return this;
    }

    @Override
    public QueryBuilderSelect where(String conditions) {
        where = conditions;
        return this;
    }

    @Override
    public QueryBuilderSelect groupBy(String statement) {
        groupBy = statement;
        return this;
    }

    @Override
    public QueryBuilderSelect having(String conditions) {
        having = conditions;
        return this;
    }

    @Override
    public QueryBuilderSelect orderBy(String statement) {
        this.orderBy = statement;
        return this;
    }

    @Override
    public QueryBuilderSelect limit(int limit, int offset) {
        this.limit = offset + ", " + limit;
        return this;
    }

    /*
        Execution
     */

    @Override
    public QueryBuilderSelect build() {
        StringBuilder builder = new StringBuilder("SELECT ");
        builder.append(columns).append(" FROM ").append(from);

        if (fromExternal != null)
            builder.append(", ").append(fromExternal);

        if (where != null)
            builder.append(" WHERE ").append(where);

        if (groupBy != null) {
            builder.append(" GROUP BY ").append(groupBy);
            if (having != null)
                builder.append(" HAVING ").append(having);
        }
        if (orderBy != null)
            builder.append(" ORDER BY ").append(orderBy);
        if (limit != null)
            builder.append(" LIMIT ").append(limit);
        setSql(builder.append(";").toString());
        return this;
    }

    @Override
    public QueryBuilderSelect exec() {
        getConnector().execute(getSql());
        return this;
    }

    @Override
    public QueryBuilderSelect patternClone() {
        QuerySelect copy = new QuerySelect(holder, databaseName);
        copy.columns = columns;
        copy.from = from;
        copy.fromExternal = fromExternal;
        copy.where = where;
        copy.groupBy = groupBy;
        copy.having = having;
        copy.orderBy = orderBy;
        copy.limit = limit;
        return copy;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public CompletableFuture<QueryBuilderSelect> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
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
    public QueryBuilderSelect execConsume(Consumer<QueryData> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderSelect> execConsumeAsync(Consumer<QueryData> digest) {
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

    @Override
    public String getSql() {
        return super.sql;
    }

}
