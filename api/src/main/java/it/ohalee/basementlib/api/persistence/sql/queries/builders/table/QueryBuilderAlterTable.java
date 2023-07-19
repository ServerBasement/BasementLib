package it.ohalee.basementlib.api.persistence.sql.queries.builders.table;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;
import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlTable;
import it.ohalee.basementlib.api.persistence.sql.structure.column.SqlType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface QueryBuilderAlterTable extends ExecutiveQuery<QueryBuilderAlterTable> {

    /**
     * Specifies a foreign key from a table's column
     * in the same database
     *
     * @param columnName  column to be used as foreign key
     * @param table       other table's name
     * @param tableColumn other column's name
     * @return self Query Builder
     */
    QueryBuilderAlterTable addForeignKey(String columnName, String table, String tableColumn);

    /**
     * Specifies a foreign key from a table's column
     * in the same database with a specified final constraint
     *
     * @param columnName  column to be used as foreign key
     * @param table       other table's name
     * @param tableColumn other column's name
     * @param constraint  full constraint
     * @return self Query Builder
     */
    QueryBuilderAlterTable addForeignKeyConstraint(String columnName, String table, String tableColumn, String constraint);

    /**
     * Specifies a foreign key from a table's column
     * in another database
     *
     * @param columnName         column to be used as foreign key
     * @param outerDB            outer database's name
     * @param outerDBTable       outer table's name
     * @param outerDBTableColumn outer column's name
     * @return self Query Builder
     */
    QueryBuilderAlterTable addForeignKey(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn);

    /**
     * Specifies a foreign key from a table's column
     * in another database with a specified final constraint
     *
     * @param columnName         column to be used as foreign key
     * @param outerDB            outer database's name
     * @param outerDBTable       outer table's name
     * @param outerDBTableColumn outer column's name
     * @param constraint         full constraint
     * @return self Query Builder
     */
    QueryBuilderAlterTable addForeignKeyConstraint(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn, String constraint);

}
