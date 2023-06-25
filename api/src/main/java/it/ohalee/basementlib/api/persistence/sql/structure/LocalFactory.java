package it.ohalee.basementlib.api.persistence.sql.structure;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface LocalFactory {

    void loadDatabase(AbstractSqlDatabase database);

    void unloadDatabase(AbstractSqlDatabase database);

    void unloadDatabase(String databaseName);

    /**
     * returns the abstract definition of
     * a database created by the ::createDatabase() method
     *
     * @param databaseName name of the database
     * @return abstract definition of the database
     */
    @Nullable AbstractSqlDatabase useDatabase(String databaseName);
}
