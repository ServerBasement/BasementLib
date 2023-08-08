package it.ohalee.basementlib.common.persistence.sql.queries.table;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderAlterTable;
import it.ohalee.basementlib.api.persistence.sql.queries.effective.SqlQuery;
import it.ohalee.basementlib.common.persistence.sql.SqlDatabase;
import it.ohalee.basementlib.common.persistence.sql.SqlTable;
import it.ohalee.basementlib.common.persistence.structure.column.ForeignKeyDefinition;

import java.sql.PreparedStatement;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public class QueryAlterTable extends SqlQuery implements QueryBuilderAlterTable {

    private final SqlDatabase database;
    private final String tableName;

    private final Deque<ForeignKeyDefinition> foreignKeys = new ArrayDeque<>();

    private SqlTable temp;

    public QueryAlterTable(SqlDatabase database, String tableName) {
        super(database.getHolder(), database.getName());
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public QueryBuilderAlterTable addForeignKey(String columnName, String table, String tableColumn) {
        addForeignKeyConstraint(columnName, table, tableColumn, null);
        return this;
    }

    @Override
    public QueryBuilderAlterTable addForeignKeyConstraint(String columnName, String table, String tableColumn, String constraint) {
        addForeignKeyConstraint(columnName, databaseName, table, tableColumn, constraint);
        return this;
    }

    @Override
    public QueryBuilderAlterTable addForeignKey(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn) {
        addForeignKeyConstraint(columnName, outerDB, outerDBTable, outerDBTableColumn, null);
        return this;
    }

    @Override
    public QueryBuilderAlterTable addForeignKeyConstraint(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn, String constraint) {
        foreignKeys.add(new ForeignKeyDefinition(columnName, outerDB, outerDBTable, outerDBTableColumn, constraint));
        return this;
    }

    @Override
    public QueryBuilderAlterTable build() {

        // ALTER TABLE Orders
        // ADD FOREIGN KEY (PersonID) REFERENCES Persons(PersonID);
        StringBuilder builder = new StringBuilder("ALTER TABLE ");
        builder.append(databaseName).append(".").append(tableName).append(" ");

        builder.append("ADD ");
        // foreign key
        int k = 0;
        while (!foreignKeys.isEmpty()) {
            ForeignKeyDefinition fkd = foreignKeys.poll();
            if (k > 0) builder.append(", ");
            builder.append("FOREIGN KEY")
                    .append("(").append(fkd.getName())
                    .append(") ").append("REFERENCES ")
                    .append(fkd.getOuterDb()).append(".").append(fkd.getOuterTable())
                    .append("(").append(fkd.getOuterColumn()).append(")");
            String constraint = fkd.getConstraint();
            if (constraint != null) {
                builder.append(" ").append(constraint);
            }
            k++;
        }

        builder.append(";");

        setSql(builder.toString());

        temp = new SqlTable(database, tableName);
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderAlterTable exec() {
        getConnector().execute(getSql());
        return this;
    }

    @Override
    public QueryBuilderAlterTable patternClone() {
        QueryAlterTable copy = new QueryAlterTable(database, tableName);
        copy.foreignKeys.addAll(foreignKeys);
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderAlterTable> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
