package it.ohalee.basementlib.common.persistence.maria.queries.table;

import it.ohalee.basementlib.api.persistence.maria.queries.builders.table.QueryBuilderDropTable;
import it.ohalee.basementlib.api.persistence.maria.queries.effective.MariaQuery;
import it.ohalee.basementlib.common.persistence.maria.structure.MariaDatabase;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;

public class QueryDropTable extends MariaQuery implements QueryBuilderDropTable {

    private final MariaDatabase database;
    private final String tableName;

    private boolean ifExists = false;

    public QueryDropTable(MariaDatabase database, String tableName) {
        super(database.getHolder(), database.getName());
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public QueryBuilderDropTable ifExists(boolean add) {
        ifExists = add;
        return this;
    }

    @Override
    public QueryBuilderDropTable build() {
        StringBuilder builder = new StringBuilder("DROP TABLE ");
        if (ifExists)
            builder.append("IF EXISTS ");
        builder.append(databaseName).append(".").append(tableName).append(";");
        setSql(builder.toString());
        return this;
    }

    @Override
    public PreparedStatement asPrepared() {
        return getConnector().asPrepared(getSql());
    }

    @Override
    public QueryBuilderDropTable exec() {
        getConnector().execute(getSql());
        database.removeTable(tableName);
        return this;
    }

    @Override
    public QueryBuilderDropTable patternClone() {
        QueryDropTable copy = new QueryDropTable(database, tableName);
        copy.ifExists = ifExists;
        return copy;
    }

    @Override
    public CompletableFuture<QueryBuilderDropTable> execAsync() {
        return CompletableFuture.supplyAsync(this::exec);
    }

    @Override
    public String getSql() {
        return super.sql;
    }

}
