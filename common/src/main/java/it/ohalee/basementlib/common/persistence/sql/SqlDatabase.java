package it.ohalee.basementlib.common.persistence.sql;

import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.*;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderAlterTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderDropTable;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderTableExists;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlTable;
import it.ohalee.basementlib.common.persistence.sql.queries.data.*;
import it.ohalee.basementlib.common.persistence.sql.queries.table.QueryAlterTable;
import it.ohalee.basementlib.common.persistence.sql.queries.table.QueryCreateTable;
import it.ohalee.basementlib.common.persistence.sql.queries.table.QueryDropTable;
import it.ohalee.basementlib.common.persistence.sql.queries.table.QueryTableExists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SqlDatabase implements AbstractSqlDatabase {

    /*
        State
     */

    @Getter
    private final AbstractSqlHolder holder;
    private final String databaseName;

    private final Map<String, AbstractSqlTable> tables = new HashMap<>();

    /*
        Internal Use
     */

    public void addTable(AbstractSqlTable table) {
        tables.put(table.getName(), table);
    }

    public void removeTable(AbstractSqlTable table) {
        removeTable(table.getName());
    }

    public void removeTable(String tableName) {
        tables.remove(tableName);
    }

    /*
        Super Use
     */

    @Override
    public String getName() {
        return databaseName;
    }

    @Override
    public QueryBuilderTableExists tableExists(String tableName) {
        return new QueryTableExists(this, tableName);
    }

    @Override
    public QueryBuilderAlterTable alterTable(String tableName) {
        return new QueryAlterTable(this, tableName);
    }

    @Override
    public QueryBuilderCreateTable createTable(String tableName) {
        return new QueryCreateTable(this, tableName);
    }

    @Override
    public QueryBuilderDropTable dropTable(String tableName) {
        return new QueryDropTable(this, tableName);
    }

    @Override
    public AbstractSqlTable useTable(String tableName) {
        return tables.get(tableName);
    }

    @Override
    public QueryBuilderSelect select() {
        return new QuerySelect(holder, databaseName);
    }

    @Override
    public QueryBuilderInsert insert() {
        return new QueryInsert(holder, databaseName);
    }

    @Override
    public QueryBuilderDelete delete() {
        return new QueryDelete(holder, databaseName);
    }

    @Override
    public QueryBuilderUpdate update() {
        return new QueryUpdate(holder, databaseName);
    }

    @Override
    public QueryBuilderReplace replace() {
        return new QueryReplace(holder, databaseName);
    }

}
