package it.ohalee.basementlib.common.persistence.maria.queries.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;
import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.QueryBuilderReplace;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QueryReplace extends MariaQuery implements QueryBuilderReplace {

    private final List<String> values = new ArrayList<>();
    private String tableName;
    private StringBuilder schema;

    public QueryReplace() {
    }

    public QueryReplace(AbstractMariaHolder holder, String database) {
        super(holder, database);
    }

    @Override
    public QueryBuilderReplace into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public QueryBuilderReplace columnSchema(String... columns) {
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
    public QueryBuilderReplace values(Object... values) {
        return valuesMap(true, values);
    }

    @Override
    public QueryBuilderReplace valuesNQ(Object... values) {
        return valuesMap(false, values);
    }

    @Override
    public QueryBuilderReplace build() {
        StringBuilder builder = new StringBuilder("REPLACE ");
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
    public QueryBuilderReplace exec() {
        getConnector().execute(getSql());
        return this;
    }

    @Override
    public QueryBuilderReplace patternClone() {
        QueryReplace copy = new QueryReplace(holder, databaseName);
        copy.tableName = tableName;
        copy.schema = schema;
        copy.values.addAll(values);
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderReplace> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

    @SuppressWarnings("unchecked")
    private QueryBuilderReplace valuesMap(boolean quoted, Object... values) {
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
                    builder.append(quoted ? getQuoted(value) : value);
                    continue;
                }
                builder.append(", ").append(quoted ? getQuoted(value) : value);
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
    public QueryBuilderReplace clearValues() {
        values.clear();
        return this;
    }
}
