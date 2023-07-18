package it.ohalee.basementlib.common.persistence.connector.hikari.maria;

import it.ohalee.basementlib.api.persistence.StorageCredentials;
import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import it.ohalee.basementlib.common.persistence.sql.SqlHolder;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class MariaFactory {

    private final AbstractSqlDatabase database;
    private final AbstractSqlHolder holder;

    public MariaFactory(AbstractBasementPlugin plugin, StorageCredentials storageCredentials) {
        Connector connector = plugin.createConnector(TypeConnector.H2, storageCredentials.minIdleConnections(), storageCredentials.maxPoolSize(), "basement");
        connector.connect(storageCredentials);

        holder = new SqlHolder(connector);
        database = holder.createDatabase(storageCredentials.database()).ifNotExists(true).build().execReturn();
    }

}
