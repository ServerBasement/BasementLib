package it.ohalee.basementlib.api.persistence.maria.queries.builders.table;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ReturningQuery;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaTable;
import it.ohalee.basementlib.api.persistence.maria.structure.column.MariaType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface QueryBuilderCreateTable extends ReturningQuery<QueryBuilderCreateTable, AbstractMariaTable> {

    /**
     * Adds a column to be add to the table
     *
     * @param columnName name of the column
     * @param type       type of the column
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName name of the column
     * @param type       type of the column
     * @param size       size applied to the type
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName name of the column
     * @param type       type of the column
     * @param size       size applied to the type
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, ColumnData... columnData);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName    name of the column
     * @param type          type of the column
     * @param autoIncrement if the column is marked as auto increment
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, boolean autoIncrement);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName name of the column
     * @param type       type of the column
     * @param columnData the data of colum like if is (auto_increment, unique, ecc...)
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, ColumnData... columnData);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName   name of the column
     * @param type         type of the column
     * @param defaultValue default value
     * @param columnData   the data of colum like if is (auto_increment, unique, ecc...)
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, String defaultValue, ColumnData... columnData);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName    name of the column
     * @param type          type of the column
     * @param size          size applied to the type
     * @param defaultValue  default value
     * @param notNull       if the column is marked as not null
     * @param autoIncrement if the column is marked as auto increment
     * @param constraint    full constraint of the column
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, String defaultValue, boolean notNull, boolean autoIncrement, String constraint);

    /**
     * Adds a column to be add to the table
     *
     * @param columnName   name of the column
     * @param type         type of the column
     * @param size         size applied to the type
     * @param defaultValue default value
     * @param constraint   full constraint of the column
     * @param columnData   the data of colum like if is (auto_increment, unique, ecc...)
     * @return self Query Builder
     */
    QueryBuilderCreateTable addColumn(String columnName, MariaType type, Integer size, String defaultValue, String constraint, ColumnData... columnData);

    /**
     * Specifies a single of bind primary key
     * based on a column already added with ::addColumn()
     *
     * @param columnName column's name
     * @return self Query Builder
     */
    QueryBuilderCreateTable withPrimaryKeys(String... columnName);

    /**
     * Specifies a foreign key from a table's column
     * in the same database
     *
     * @param columnName  column to be used as foreign key
     * @param table       other table's name
     * @param tableColumn other column's name
     * @return self Query Builder
     */
    QueryBuilderCreateTable addForeignKey(String columnName, String table, String tableColumn);

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
    QueryBuilderCreateTable addForeignKeyConstraint(String columnName, String table, String tableColumn, String constraint);

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
    QueryBuilderCreateTable addForeignKey(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn);

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
    QueryBuilderCreateTable addForeignKeyConstraint(String columnName, String outerDB, String outerDBTable, String outerDBTableColumn, String constraint);

    /**
     * if true it includes
     * OR REPLACE
     * block in the final query
     *
     * @param add if the block must be present
     * @return self Query Builder
     */
    QueryBuilderCreateTable orReplace(boolean add);

    /**
     * if true it includes
     * IF NOT EXISTS
     * block in the final query
     *
     * @param add if the block must be present
     * @return self Query Builder
     */
    QueryBuilderCreateTable ifNotExists(boolean add);

    @Getter
    @RequiredArgsConstructor
    enum ColumnData {
        AUTO_INCREMENT("AUTO_INCREMENT"),
        UNIQUE("UNIQUE"),
        NOT_NULL("NOT NULL"),
        NULL("NULL");

        private final String name;
    }
}
