package it.ohalee.basementlib.common.persistence.maria.queries.table;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaTable;
import it.ohalee.basementlib.api.persistence.maria.structure.column.MariaType;
import it.ohalee.basementlib.common.persistence.maria.structure.MariaDatabase;
import it.ohalee.basementlib.common.persistence.maria.structure.MariaTable;
import it.ohalee.basementlib.common.persistence.maria.structure.column.ForeignKeyDefinition;
import it.ohalee.basementlib.common.persistence.maria.structure.column.MariaColumn;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class QueryCreateTable extends MariaQuery implements QueryBuilderCreateTable {

    private final MariaDatabase database;
    private final String tableName;

    private final Deque<String> primaryKeys = new ArrayDeque<>();
    private final Deque<ForeignKeyDefinition> foreignKeys = new ArrayDeque<>();
    private final Deque<MariaColumn> columns = new ArrayDeque<>();

    private boolean orReplace = false;
    private boolean ifNotExists = false;
    private MariaTable temp;

    public QueryCreateTable(MariaDatabase database, String tableName) {
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
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type) {
        addColumn(columnName, type, new ColumnData[0]);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size) {
        addColumn(columnName, type, size, new ColumnData[0]);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, ColumnData... columnData) {
        addColumn(columnName, type, size, null, null, columnData);
        return this;
    }

    @Deprecated
    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, boolean autoIncrement) {
        List<ColumnData> columnData = new ArrayList<>();
        if (autoIncrement) columnData.add(ColumnData.AUTO_INCREMENT);
        addColumn(columnName, type, null, null, null, columnData.toArray(new ColumnData[0]));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, ColumnData... columnData) {
        addColumn(columnName, type, null, null, null, columnData);
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, String defaultValue, ColumnData... columnData) {
        addColumn(columnName, type, null, defaultValue, null, columnData);
        return this;
    }

    @Deprecated
    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, String defaultValue, boolean notNull, boolean autoIncrement, String constraint) {
        List<ColumnData> columnData = new ArrayList<>();
        if (notNull) columnData.add(ColumnData.NOT_NULL);
        if (autoIncrement) columnData.add(ColumnData.AUTO_INCREMENT);
        addColumn(columnName, type, size, defaultValue, constraint, columnData.toArray(new ColumnData[0]));
        return this;
    }

    @Override
    public QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, String defaultValue, String constraint, ColumnData... columnData) {
        columns.add(new MariaColumn(columnName, type, size, defaultValue, constraint, columnData));
        return this;
    }

    @Override
    public QueryBuilderCreateTable build() {

        StringBuilder builder = new StringBuilder("CREATE TABLE").append(" ");

        if (ifNotExists)
            builder.append("IF NOT EXISTS").append(" ");
        else if (orReplace)
            builder.append("OR REPLACE").append(" ");

        builder.append(databaseName).append(".").append(tableName).append(" (");

        // columns

        columns.forEach(column -> builder.append(column.toString()).append(", "));

        // primary key
        builder.append("primary key").append("(");

        while (!primaryKeys.isEmpty()) {
            String key = primaryKeys.poll();
            builder.append(key);
            if (!primaryKeys.isEmpty())
                builder.append(",");
        }

        builder.append(")");

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

        temp = new MariaTable(database, tableName);

        List<String> fk = foreignKeys.stream().map(ForeignKeyDefinition::getName).collect(Collectors.toList());

        columns.forEach(column -> {
            temp.addColumn(column);
            String columnName = column.getName();
            if (primaryKeys.contains(columnName)) {
                temp.addPrimaryKey(column);
            }
            if (fk.contains(columnName)) {
                temp.addForeignKey(column);
            }
        });

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
    public AbstractMariaTable execReturn() {
        exec();
        return temp;
    }

    @Override
    public CompletableFuture<AbstractMariaTable> execReturnAsync() {
        return CompletableFuture.supplyAsync(this::execReturn);
    }

    @Override
    public QueryBuilderCreateTable execConsume(Consumer<AbstractMariaTable> digest) {
        digest.accept(execReturn());
        return this;
    }

    @Override
    public CompletableFuture<QueryBuilderCreateTable> execConsumeAsync(Consumer<AbstractMariaTable> digest) {
        return CompletableFuture.supplyAsync(() -> execConsume(digest));
    }

    @Override
    public AbstractMariaTable execReturnAfter(UnaryOperator<AbstractMariaTable> action) {
        return action.apply(execReturn());
    }

    @Override
    public CompletableFuture<AbstractMariaTable> execReturnAfterAsync(UnaryOperator<AbstractMariaTable> action) {
        return CompletableFuture.supplyAsync(() -> execReturnAfter(action));
    }

}
