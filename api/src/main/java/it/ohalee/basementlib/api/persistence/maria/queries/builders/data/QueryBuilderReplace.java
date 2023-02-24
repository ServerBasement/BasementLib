package it.ohalee.basementlib.api.persistence.maria.queries.builders.data;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;

public interface QueryBuilderReplace extends ExecutiveQuery<QueryBuilderReplace> {

    QueryBuilderReplace into(String tableName);

    QueryBuilderReplace columnSchema(String... columns);

    QueryBuilderReplace values(Object... values);

    QueryBuilderReplace valuesNQ(Object... values);

    QueryBuilderReplace clearValues();

}
