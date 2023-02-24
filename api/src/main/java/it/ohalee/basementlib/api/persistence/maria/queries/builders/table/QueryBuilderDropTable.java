package it.ohalee.basementlib.api.persistence.maria.queries.builders.table;

import it.ohalee.basementlib.api.persistence.generic.queries.effective.ExecutiveQuery;

/**
 * Builder representing a 'DROP TABLE' Query
 */
public interface QueryBuilderDropTable extends ExecutiveQuery<QueryBuilderDropTable> {

    /**
     * if true it includes
     * IF EXISTS
     * block in the final query
     *
     * @param add if the block must be present
     * @return self Query Builder
     */
    QueryBuilderDropTable ifExists(boolean add);

}
