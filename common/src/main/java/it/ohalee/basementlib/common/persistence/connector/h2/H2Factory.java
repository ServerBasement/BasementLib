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
import java.util.List;

@Getter
@Accessors(fluent = true)
public class H2Factory implements LocalFactory {

    private final AbstractBasementPlugin plugin;
    private final Path folder;
    private final AbstractSqlHolder holder;

    private final List<Connector> connectors = new ArrayList<>();

    public H2Factory(AbstractBasementPlugin plugin, Path folder) {
        this.plugin = plugin;
        this.folder = folder;

        this.holder = new SqlHolder(null);
    }


    @Override
    public synchronized @Nullable AbstractSqlDatabase useDatabase(String databaseName) {
        AbstractSqlDatabase database = holder.useDatabase(databaseName);
        if (database != null) {
            return database;
        }

        Connector connector = plugin.createConnector(TypeConnector.H2, -1, -1, null);
        connector.connect(databaseName + ".mv.db");

        database = new SqlDatabase(holder, databaseName);
        holder.loadDatabase(database);

        connectors.add(connector);
        return database;
    }

    @Override
    public void loadDatabase(AbstractSqlDatabase database) {
        holder.loadDatabase(database);
    }

    @Override
    public void unloadDatabase(AbstractSqlDatabase database) {
        holder.unloadDatabase(database);
    }

    @Override
    public void unloadDatabase(String databaseName) {
        holder.unloadDatabase(databaseName);
    }

    public void shutdown() {
        connectors.forEach(Connector::close);
    }

}
