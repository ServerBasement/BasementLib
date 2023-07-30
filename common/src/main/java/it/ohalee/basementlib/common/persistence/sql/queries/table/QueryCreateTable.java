package it.ohalee.basementlib.common.persistence.sql.queries.table;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.sql.queries.effective.SqlQuery;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlTable;
import it.ohalee.basementlib.api.persistence.sql.structure.column.SqlType;
import it.ohalee.basementlib.common.persistence.sql.SqlColumn;
import it.ohalee.basementlib.common.persistence.sql.SqlDatabase;
import it.ohalee.basementlib.common.persistence.sql.SqlTable;
import it.ohalee.basementlib.common.persistence.structure.column.ForeignKeyDefinition;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class QueryCreateTable extends SqlQuery implements QueryBuilderCreateTable {

    private final SqlDatabase database;
    private final String tableName;

    private final Deque<String> primaryKeys = new ArrayDeque<>();
    private final Deque<ForeignKeyDefinition> foreignKeys = new ArrayDeque<>();
    private final Deque<SqlColumn> columns = new ArrayDeque<>();

    private boolean orReplace = false;
    private boolean ifNotExists = false;
    private SqlTable temp;

    public QueryCreateTable(SqlDatabase database, String tableName) {
        super(database.getHolder(), database.getName());
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public QueryBuilderCreateTable orReplace(boolean add) {
        orReplace = add;
        return this;
    }

    @Override
    public QueryBuilderCreateTable ifNotExists(boolean add) {
        ifNotExists = add;
        return this;
    }

    @Override
    public QueryBuilderCreateTable withPrimaryKeys(String... columnName) {
        primaryKeys.addAll(Arrays.asList(columnName));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addForeignKey(String columnName, String table, String tableColumn) {
        addForeignKeyConstraint(columnName, table, tableColumn, null);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addForeignKeyConstraint(String columnName, String table, String tableColumn, String constraint) {
        addForeignKeyConstraint(columnName, databaseName, table, tableColumn, constraint);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addForeignKey(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn) {
        addForeignKeyConstraint(columnName, outerDB, outerDBTable, outerDBTableColumn, null);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addForeignKeyConstraint(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn, String constraint) {
        foreignKeys.add(new ForeignKeyDefinition(columnName, outerDB, outerDBTable, outerDBTableColumn, constraint));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type) {
        addColumn(columnName, type, new ColumnData[0]);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, Integer size) {
        addColumn(columnName, type, size, new ColumnData[0]);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, Integer size, ColumnData... columnData) {
        addColumn(columnName, type, size, null, null, columnData);
        return this;
    }

    @Deprecated
    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, boolean autoIncrement) {
        List<ColumnData> columnData = new ArrayList<>();
        if (autoIncrement) columnData.add(ColumnData.AUTO_INCREMENT);
        addColumn(columnName, type, null, null, null, columnData.toArray(new ColumnData[0]));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, ColumnData... columnData) {
        addColumn(columnName, type, null, null, null, columnData);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, String defaultValue, ColumnData... columnData) {
        addColumn(columnName, type, null, defaultValue, null, columnData);
        return this;
    }

    @Deprecated
    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, Integer size, String defaultValue, boolean notNull, boolean autoIncrement, String constraint) {
        List<ColumnData> columnData = new ArrayList<>();
        if (notNull) columnData.add(ColumnData.NOT_NULL);
        if (autoIncrement) columnData.add(ColumnData.AUTO_INCREMENT);
        addColumn(columnName, type, size, defaultValue, constraint, columnData.toArray(new ColumnData[0]));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, SqlType type, Integer size, String defaultValue, String constraint, ColumnData... columnData) {
        columns.add(new SqlColumn(getConnector().getType(), columnName, type, size, defaultValue, constraint, columnData));
        return this;
    }

    @Override
    public QueryBuilderCreateTable build() {

        StringBuilder builder = new StringBuilder("CREATE TABLE").append(" ");

        if (ifNotExists)
            builder.append("IF NOT EXISTS").append(" ");
        else if (orReplace && !holder.isH2()) // Replace is not supported by H2
            builder.append("OR REPLACE").append(" ");

        builder.append(databaseName).append(".").append(tableName).append(" (");

        // columns
        SqlColumn[] columns = this.columns.toArray(new SqlColumn[0]);
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i].toString());
            if (i < columns.length - 1)
                builder.append(", ");
        }

        // primary key
        if (!primaryKeys.isEmpty()) {
            builder.append(", ").append("primary key").append("(");

            while (!primaryKeys.isEmpty()) {
                String key = primaryKeys.poll();
                builder.append(key);
                if (!primaryKeys.isEmpty())
                    builder.append(",");
            }

            builder.append(")");
        }

        // foreign key
        while (!foreignKeys.isEmpty()) {
            ForeignKeyDefinition fkd = foreignKeys.poll();
            builder.append(", ").append("FOREIGN KEY")
                    .append("(").append(fkd.getName())
                    .append(") ").append("REFERENCES ")
                    .append(fkd.getOuterDb()).append(".").append(fkd.getOuterTable())
                    .append("(").append(fkd.getOuterColumn()).append(")");
            String constraint = fkd.getConstraint();
            if (constraint != null) {
                builder.append(" ").append(constraint);
            }
        }

        builder.append(");");

        setSql(builder.toString());

        temp = new SqlTable(database, tableName);
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderCreateTable exec() {
        getConnector().execute(getSql());
        database.addTable(temp);
        return this;
    }

    @Override
    public QueryBuilderCreateTable patternClone() {
        QueryCreateTable copy = new QueryCreateTable(database, tableName);
        copy.primaryKeys.addAll(primaryKeys);
        copy.foreignKeys.addAll(foreignKeys);
        copy.columns.addAll(columns);
        copy.orReplace = orReplace;
        copy.ifNotExists = ifNotExists;
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateTable> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

    @Override
    public AbstractSqlTable execReturn() {
        exec();
        return temp;
    }

    @Override
    public CompletableFuture<AbstractSqlTable> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }

    @Override
    public QueryBuilderCreateTable execConsume(Consumer<AbstractSqlTable> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateTable> execConsumeAsync(Consumer<AbstractSqlTable> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public AbstractSqlTable execReturnAfter(UnaryOperator<AbstractSqlTable> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<AbstractSqlTable> execReturnAfterAsync(UnaryOperator<AbstractSqlTable> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }

}
