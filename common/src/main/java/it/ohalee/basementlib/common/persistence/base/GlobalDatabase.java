package it.ohalee.basementlib.common.persistence.base;

import it.ohalee.basementlib.api.BasementProvider;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.WhereBuilder;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.QueryBuilderDelete;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.QueryBuilderInsert;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.QueryBuilderSelect;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.data.QueryBuilderUpdate;
import it.ohalee.basementlib.api.persistence.sql.queries.builders.table.QueryBuilderCreateTable;
import it.ohalee.basementlib.api.persistence.sql.structure.AbstractSqlDatabase;
import it.ohalee.basementlib.api.persistence.sql.structure.column.SqlType;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GlobalDatabase {

    private final QueryBuilderInsert createPlayer;
    private final QueryBuilderInsert createServer;

    private final QueryBuilderDelete deleteServer;

    private final QueryBuilderSelect playerData;

    private final QueryBuilderUpdate updateServer;

    public GlobalDatabase(AbstractSqlDatabase database) {
        if (!database.tableExists("basement_players").execReturn()) {
            database.createTable("basement_players").ifNotExists(true)
                    .addColumn("id", SqlType.INT, QueryBuilderCreateTable.ColumnData.AUTO_INCREMENT)
                    .addColumn("uuid", SqlType.VARCHAR, 36, QueryBuilderCreateTable.ColumnData.NOT_NULL, QueryBuilderCreateTable.ColumnData.UNIQUE)
                    .addColumn("username", SqlType.VARCHAR, 16, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .withPrimaryKeys("id").build().exec();

            database.createTable("basement_servers").ifNotExists(true)
                    .addColumn("id", SqlType.INT, QueryBuilderCreateTable.ColumnData.AUTO_INCREMENT)
                    .addColumn("name", SqlType.VARCHAR, 32, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .addColumn("uuid", SqlType.VARCHAR, 36, QueryBuilderCreateTable.ColumnData.NOT_NULL, QueryBuilderCreateTable.ColumnData.UNIQUE)
                    .addColumn("java", SqlType.VARCHAR, 32, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .addColumn("version", SqlType.VARCHAR, 32, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .addColumn("cpu_usage", SqlType.FLOAT, "0")
                    .addColumn("ram_usage", SqlType.FLOAT, "0")
                    .addColumn("mspt", SqlType.FLOAT, "0")
                    .addColumn("online_players", SqlType.INT, "0")
                    .addColumn("max_players", SqlType.INT, "0")
                    .addColumn("start_time", SqlType.DATETIME, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .addColumn("status", SqlType.INT, 1, QueryBuilderCreateTable.ColumnData.NOT_NULL)
                    .withPrimaryKeys("id").build().exec();
        }

        this.createPlayer = database.insert().ignore(true).into("basement_players").columnSchema("uuid", "username");
        this.playerData = database.select().columns("id", "username", "uuid").from("basement_players");

        this.createServer = database.insert().ignore(true).into("basement_servers").columnSchema("name", "uuid", "java", "version", "start_time", "status");
        this.deleteServer = database.delete().from("basement_servers");

        this.updateServer = database.update().table("basement_servers");
    }

    public CompletableFuture<Boolean> registerPlayer(String username, UUID uniqueId) {
        return playerData.patternClone().where(WhereBuilder.builder().equals("uuid", uniqueId.toString()).close()).build()
                .execReturnAsync().thenApply(queryData -> {
                    if (!queryData.first()) {
                        createPlayer.values(uniqueId.toString(), username).build().exec();
                        return true;
                    }
                    return false;
        });
    }

    public CompletableFuture<Void> registerServer(String name, String version) {
        return deleteServer.patternClone().where(WhereBuilder.builder().equals("uuid", BasementProvider.get().uuid().toString()).close()).build()
                .execAsync().thenApply(useless -> {
                    createServer.values(name, BasementProvider.get().uuid().toString(), System.getProperty("java.version"), version, new Timestamp(System.currentTimeMillis()), 1).build().exec();
                    return null;
        });
    }

    public void updateServer(int onlinePlayers, int maxPlayers) {
        updateServer.patternClone()
                .set("cpu_usage", StatsUsage.cpu())
                .set("ram_usage", StatsUsage.ram())
                .set("mspt", StatsUsage.mspt())
                .set("online_players", onlinePlayers)
                .set("max_players", maxPlayers)
                .where(WhereBuilder.builder().equals("uuid", BasementProvider.get().uuid()).close()).build().execAsync();
    }

    public void updateOfflineServer() {
        updateServer.patternClone()
                .set("cpu_usage", 0)
                .set("ram_usage", 0)
                .set("mspt", 0)
                .set("online_players", 0)
                .set("max_players", 0)
                .set("status", 0)
                .where(WhereBuilder.builder().equals("uuid", BasementProvider.get().uuid()).close()).build().exec();
    }

}