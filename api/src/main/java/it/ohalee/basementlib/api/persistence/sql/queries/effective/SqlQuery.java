package it.ohalee.basementlib.api.persistence.sql.queries.effective;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import lombok.Getter;
import lombok.Setter;

/**
 * represents the form of the query ready to run
 */
@Setter
public abstract class SqlQuery {

    /**
     * main Holder, provider of the future connector.
     */
    @Getter
    protected AbstractSqlHolder holder;
    /**
     * Name of the present of future database
     */
    @Getter
    protected String databaseName;
    /**
     * The actual query
     */
    protected String sql;

    public SqlQuery() {
    }

    public SqlQuery(AbstractSqlHolder holder, String databaseName) {
        this.holder = holder;
        this.databaseName = databaseName;
    }

    protected Connector getConnector() {
        return holder.getConnector();
    }

}
