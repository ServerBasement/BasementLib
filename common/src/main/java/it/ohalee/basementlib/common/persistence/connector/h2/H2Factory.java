package it.ohalee.basementlib.common.persistence.connector.h2;

import it.ohalee.basementlib.api.persistence.generic.connection.Connector;
import it.ohalee.basementlib.api.persistence.generic.connection.TypeConnector;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlHolder;
import it.ohalee.basementlib.api.persistence.sql.structure.LocalFactory;
import it.ohalee.basementlib.common.persistence.sql.SqlDatabase;
import it.ohalee.basementlib.common.persistence.sql.SqlHolder;
import it.ohalee.basementlib.common.plugin.AbstractBasementPlugin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public class H2Factory {

    private final AbstractBasementPlugin plugin;
    private final Path folder;

    private final Map<String, AbstractSqlDatabase> databases = new HashMap<>();
    private final List<Connector> connectors = new ArrayList<>();

    public H2Factory(AbstractBasementPlugin plugin, Path folder) {
        this.plugin = plugin;
        this.folder = folder;
    }

    public synchronized @Nullable AbstractSqlDatabase useDatabase(String databaseName) {
        AbstractSqlDatabase database = databases.get(databaseName);
        if (database != null) {
            return database;
        }

        Connector connector = plugin.createConnector(TypeConnector.H2, -1, -1, null);
        connector.connect(databaseName + ".mv.db");

        AbstractSqlHolder holder = new SqlHolder(connector);

        database = new SqlDatabase(holder, databaseName);
        holder.loadDatabase(database);

        connectors.add(connector);
        databases.put(databaseName, database);
        return database;
    }

    public void shutdown() {
        connectors.forEach(Connector::close);
    }

}
