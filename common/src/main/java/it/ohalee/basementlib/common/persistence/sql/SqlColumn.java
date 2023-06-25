package it.ohalee.basementlib.common.persistence.sql;

import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.sql.structure.column.SqlType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SqlColumn {

    private final TypeConnector connector;

    private final String name;
    private final SqlType type;
    private final Integer size;

    private final String defaultValue;
    private final String constraint;
    private final QueryBuilderCreateTable.ColumnData[] columnData;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder(name).append(" ");

        builder.append(connector == TypeConnector.H2 ? type.h2() : type.maria()).append(" ");

        if (size != null)
            builder.append("(").append(size).append(")").append(" ");

        for (QueryBuilderCreateTable.ColumnData columnDatum : columnData) {
            if (columnDatum == null) continue;
            builder.append(columnDatum.getName()).append(" ");
        }


        if (defaultValue != null)
            builder.append("DEFAULT ").append(defaultValue).append(" ");

        if (constraint != null)
            builder.append(constraint);

        return builder.toString();
    }

}
