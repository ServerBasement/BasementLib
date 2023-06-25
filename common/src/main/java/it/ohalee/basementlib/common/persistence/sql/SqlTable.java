package it.ohalee.basementlib.common.persistence.sql;

import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlTable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqlTable implements AbstractSqlTable {

    private final AbstractSqlDatabase database;
    private final String tableName;

    /*
        Super Use
     */

    @Override
    public String getName() {
        return tableName;
    }

    @Override
    public AbstractSqlDatabase getDatabase() {
        return database;
    }

}
