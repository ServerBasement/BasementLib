package it.ohalee.basementlib.api.persistence.sql.structure.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum SqlType {

    VARCHAR("VARCHAR", "VARCHAR"), // size
    TEXT("TEXT", null),
    TINYINT("TINYINT", "TINYINT"),
    BOOL("BOOL", "BOOLEAN"),
    SMALLINT("SMALLINT", "SMALLINT"),
    MEDIUMINT("MEDIUMINT", null),
    INT("INT", "INTEGER"),
    BIGINT("BIGINT", "BIGINT"),
    FLOAT("FLOAT", "REAL"), // size (n, m)
    DOUBLE("DOUBLE", "DOUBLE PRECISION"), // size (n, m)
    DECIMAL("DECIMAL", "DECFLOAT"), // size (n, m)
    DATE("DATE", "DATE"),
    DATETIME("DATETIME", null),
    TIMESTAMP("TIMESTAMP", "TIMESTAMP"),
    TIME("TIME", "TIME"),
    YEAR("YEAR", null);

    private final String maria;
    private final String h2;

}
