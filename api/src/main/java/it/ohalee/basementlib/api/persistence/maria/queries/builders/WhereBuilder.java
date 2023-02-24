package it.ohalee.basementlib.api.persistence.maria.queries.builders;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;
import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;

public class WhereBuilder {

    private final StringBuilder buffer = new StringBuilder();

    public static WhereBuilder builder() {
        return new WhereBuilder();
    }

    private String getQuoted(Object value) {
        if (value instanceof Boolean)
            return (boolean) value ? "1" : "0";
        return "'" + value + "'";
    }

    private String getSubQuery(String query) {
        return "(" + query.replace(";", "") + ")";
    }

    public WhereBuilder sub(String sub) {
        buffer.append("(").append(sub).append(")").append(" ");
        return this;
    }

    public WhereBuilder equals(String column, Object value) {
        return equals(column, value, true);
    }

    public WhereBuilder equalsNQ(String column, Object value) {
        return equals(column, value, false);
    }

    private WhereBuilder equals(String column, Object value, boolean quoted) {
        buffer.append(column).append("=").append(quoted ? getQuoted(value) : value).append(" ");
        return this;
    }

    public WhereBuilder equals(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("=").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder safeEquals(String column, Object value) {
        return safeEquals(column, value, true);
    }

    public WhereBuilder safeEqualsNQ(String column, Object value) {
        return safeEquals(column, value, false);
    }

    private WhereBuilder safeEquals(String column, Object value, boolean quoted) {
        buffer.append(column).append("<=>").append(quoted ? getQuoted(value) : value).append(" ");
        return this;
    }

    public WhereBuilder safeEquals(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("<=>").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder notEquals(String column, Object value) {
        return notEquals(column, value, true);
    }

    public WhereBuilder notEqualsNQ(String column, Object value) {
        return notEquals(column, value, false);
    }

    private WhereBuilder notEquals(String column, Object value, boolean quoted) {
        buffer.append(column).append("!=").append(quoted ? getQuoted(value) : value).append(" ");
        return this;
    }

    public WhereBuilder notEquals(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("!=").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder safeNotEquals(String column, Object value) {
        return safeNotEquals(column, value, true);
    }

    public WhereBuilder safeNotEqualsNQ(String column, Object value) {
        return safeNotEquals(column, value, false);
    }

    private WhereBuilder safeNotEquals(String column, Object value, boolean quoted) {
        buffer.append(column).append("<>").append(quoted ? getQuoted(value) : value).append(" ");
        return this;
    }

    public WhereBuilder safeNotEquals(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("<>").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder greaterThan(String column, Number value) {
        buffer.append(column).append(">").append(value).append(" ");
        return this;
    }

    public WhereBuilder greaterThan(String column, String value) {
        buffer.append(column).append(">").append(value).append(" ");
        return this;
    }

    public WhereBuilder greaterThan(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append(">").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder greaterEqual(String column, Number value) {
        buffer.append(column).append(">=").append(value).append(" ");
        return this;
    }

    public WhereBuilder greaterEqual(String column, String value) {
        buffer.append(column).append(">=").append(value).append(" ");
        return this;
    }

    public WhereBuilder greaterEqual(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append(">=").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder lessThan(String column, Number value) {
        buffer.append(column).append("<").append(value).append(" ");
        return this;
    }

    public WhereBuilder lessThan(String column, String value) {
        buffer.append(column).append("<").append(value).append(" ");
        return this;
    }

    public WhereBuilder lessThan(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("<").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder lessEqual(String column, Number value) {
        buffer.append(column).append("<=").append(value).append(" ");
        return this;
    }

    public WhereBuilder lessEqual(String column, String value) {
        buffer.append(column).append("<=").append(value).append(" ");
        return this;
    }

    public WhereBuilder lessEqual(String column, ReturningQuery<? extends ExecutiveQuery<?>, ?> value) {
        buffer.append(column).append("<=").append(getSubQuery(value.build().getSql())).append(" ");
        return this;
    }

    public WhereBuilder not() {
        buffer.append("NOT").append(" ");
        return this;
    }

    public WhereBuilder and() {
        buffer.append("AND").append(" ");
        return this;
    }

    public WhereBuilder or() {
        buffer.append("OR").append(" ");
        return this;
    }

    public WhereBuilder in(String column, String... values) {
        buffer.append(column).append(" ").append("IN (");
        if (values.length != 0) buffer.append(getQuoted(values[0]));
        for (int i = 1; i < values.length; i++) {
            buffer.append(", ").append(getQuoted(values[i]));
        }
        buffer.append(")").append(" ");
        return this;
    }

    public WhereBuilder in(String column, Number... values) {
        buffer.append(column).append(" ").append("IN (");
        if (values.length != 0) buffer.append(getQuoted(values[0]));
        for (int i = 1; i < values.length; i++) {
            buffer.append(", ").append(values[i]);
        }
        buffer.append(")").append(" ");
        return this;
    }

    public WhereBuilder between(String column, Number lb, Number ub) {
        buffer.append(column).append(" ").append("BETWEEN").append(" ")
                .append(lb).append(" AND ").append(ub).append(" ");
        return this;
    }

    public WhereBuilder isNull(String column) {
        buffer.append(column).append(" ").append("IS NULL").append(" ");
        return this;
    }

    public WhereBuilder isNotNull(String column) {
        buffer.append(column).append(" ").append("IS NOT NULL").append(" ");
        return this;
    }

    public WhereBuilder like(String column, String pattern) {
        buffer.append(column).append(" ").append("LIKE '").append(pattern).append("' ");
        return this;
    }

    public String close() {
        return this.buffer.toString();
    }

}
