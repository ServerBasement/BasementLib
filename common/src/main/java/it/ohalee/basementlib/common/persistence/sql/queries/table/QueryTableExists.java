package it.ohalee.basementlib.common.persistence.sql.queries.table;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderAlterTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderTableExists;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderTableExists;
import it.ohalee.basementlib.api.persistence.sql.queries.effective.SqlQuery;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlTable;
import it.ohalee.basementlib.common.persistence.sql.SqlDatabase;
import it.ohalee.basementlib.common.persistence.sql.SqlTable;
import it.ohalee.basementlib.common.persistence.structure.column.ForeignKeyDefinition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QueryTableExists extends SqlQuery implements QueryBuilderTableExists {

    private final SqlDatabase database;
    private final String tableName;

    public QueryTableExists(SqlDatabase database, String tableName) {
        super(database.getHolder(), database.getName());
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public QueryBuilderTableExists build() {
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return null;
    }

    @Override
    public QueryBuilderTableExists exec() {
        return this;
    }

    @Override
    public QueryBuilderTableExists patternClone() {
        return new QueryTableExists(database, tableName);
    }

    @Override
    public CompletableFuture<QueryBuilderTableExists> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return null;
    }

    @Override
    public Boolean execReturn() {
        try (ResultSet rs = getConnector().getMetaData().getTables(getConnector().getCatalog(), null, "%", null)) {
            while (rs.next()) {
                if (rs.getString(3).equalsIgnoreCase(tableName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Boolean> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }

    @Override
    public QueryBuilderTableExists execConsume(Consumer<Boolean> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderTableExists> execConsumeAsync(Consumer<Boolean> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public Boolean execReturnAfter(UnaryOperator<Boolean> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<Boolean> execReturnAfterAsync(UnaryOperator<Boolean> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }
}
