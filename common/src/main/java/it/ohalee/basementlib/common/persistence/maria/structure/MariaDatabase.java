package it.ohalee.basementlib.common.persistence.maria.structure;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.data.*;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderDropTable;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaDatabase;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaTable;
import it.ohalee.basementlib.common.persistence.maria.queries.data.*;
import it.ohalee.basementlib.common.persistence.maria.queries.table.QueryCreateTable;
import it.ohalee.basementlib.common.persistence.maria.queries.table.QueryDropTable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MariaDatabase implements AbstractMariaDatabase {

    /*
        State
     */

    @Getter
    private final AbstractMariaHolder holder;
    private final String databaseName;

    private final Map<String, AbstractMariaTable> tables = new HashMap<>();

    /*
        Internal Use
     */

    public void addTable(AbstractMariaTable table) {
        tables.put(table.getName(), table);
    }

    public void removeTable(AbstractMariaTable table) {
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
    public QueryBuilderCreateTable createTable(String tableName) {
        return new QueryCreateTable(this, tableName);
    }

    @Override
    public QueryBuilderDropTable dropTable(String tableName) {
        return new QueryDropTable(this, tableName);
    }

    @Override
    public AbstractMariaTable useTable(String tableName) {
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
