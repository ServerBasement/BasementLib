package it.ohalee.basementlib.common.persistence.maria.queries.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;
import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.QueryBuilderInsert;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QueryInsert extends MariaQuery implements QueryBuilderInsert {

    private final List<String> values = new ArrayList<>();
    private String tableName;
    private StringBuilder schema;
    private boolean ignore = true;

    public QueryInsert() {
    }

    public QueryInsert(AbstractMariaHolder holder, String database) {
        super(holder, database);
    }

    @Override
    public QueryBuilderInsert into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public QueryBuilderInsert ignore(boolean add) {
        ignore = add;
        return this;
    }

    @Override
    public QueryBuilderInsert columnSchema(String... columns) {
        schema = new StringBuilder();
        schema.append("(");
        boolean first = true;
        for (String column : columns) {
            if (first) {
                first = false;
                schema.append(column);
                continue;
            }
            schema.append(", ").append(column);
        }
        schema.append(")");
        return this;
    }

    @Override
    public QueryBuilderInsert values(Object... values) {
        return valuesMap(true, values);
    }

    @Override
    public QueryBuilderInsert valuesNQ(Object... values) {
        return valuesMap(false, values);
    }

    @SuppressWarnings("unchecked")
    private QueryBuilderInsert valuesMap(boolean quoted, Object... values) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        boolean first = true;
        for (Object value : values) {
            if (value instanceof ReturningQuery) {
                ReturningQuery<? extends ExecutiveQuery<?>, ?> castedValue = (ReturningQuery<? extends ExecutiveQuery<?>, ?>) value;
                if (first) {
                    first = false;
                    builder.append(getSubQuery(castedValue.build().getSql().replace(";", "")));
                    continue;
                }
                builder.append(", ").append(getSubQuery(castedValue.build().getSql().replace(";", "")));
            } else {
                if (first) {
                    first = false;
                    if (value == null) {
                        builder.append("NULL");
                    } else {
                        builder.append(quoted ? getQuoted(value) : value);
                    }
                    continue;
                }
                if (value == null) {
                    builder.append(", NULL");
                } else {
                    builder.append(", ").append(quoted ? getQuoted(value) : value);
                }
            }
        }
        builder.append(")");
        this.values.add(builder.toString());
        return this;
    }

    private String getQuoted(Object value) {
        if (value instanceof Boolean) {
            return (boolean) value ? "1" : "0";
        }
        return "'" + value + "'";
    }

    private String getSubQuery(String query) {
        return "(" + query + ")";
    }

    @Override
    public QueryBuilderInsert build() {
        StringBuilder builder = new StringBuilder("INSERT ");
        if (ignore)
            builder.append("IGNORE ");
        builder.append("INTO ");
        builder.append(databaseName).append(".").append(tableName);
        if (schema != null)
            builder.append(schema);
        builder.append(" VALUES ");
        boolean first = true;
        for (String value : values) {
            if (first) {
                first = false;
                builder.append(value);
                continue;
            }
            builder.append(", ").append(value);
        }
        setSql(builder.append(";").toString());
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderInsert exec() {
        getConnector().execute(getSql());
        return this;
    }

    @Override
    public QueryBuilderInsert patternClone() {
        QueryInsert copy = new QueryInsert(holder, databaseName);
        copy.tableName = tableName;
        copy.schema = schema;
        copy.ignore = ignore;
        copy.values.addAll(values);
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderInsert> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
