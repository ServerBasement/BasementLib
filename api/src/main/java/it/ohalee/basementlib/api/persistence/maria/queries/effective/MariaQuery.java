package it.ohalee.basementlib.api.persistence.maria.queries.effective;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.maria.structure.AbstractMariaHolder;
import lombok.Getter;
import lombok.Setter;

/**
 * represents the form of the query ready to run
 */
@Setter
public abstract class MariaQuery {

    /**
     * main Holder, provider of the future connector.
     */
    @Getter
    protected AbstractMariaHolder holder;
    /**
     * Name of the present of future database
     */
    @Getter
    protected String databaseName;
    /**
     * The actual query
     */
    protected String sql;

    public MariaQuery() {
    }

    public MariaQuery(AbstractMariaHolder holder, String databaseName) {
        this.holder = holder;
        this.databaseName = databaseName;
    }

    protected Connector getConnector() {
        return holder.getConnector();
    }

}
