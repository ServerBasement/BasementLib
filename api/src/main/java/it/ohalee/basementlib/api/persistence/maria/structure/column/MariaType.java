package it.ohalee.basementlib.api.persistence.maria.structure.column;

/**
 * represents the main
 * types in mariasql
 */
public enum MariaType {

    VARCHAR, // size
    TEXT,
    TINYINT,
    BOOL,
    SMALLINT,
    MEDIUMINT,
    INT,
    BIGINT,
    FLOAT, // size (n, m)
    DOUBLE, // size (n, m)
    DECIMAL, // size (n, m)
    DATE,
    DATETIME,
    TIMESTAMP,
    TIME,
    YEAR

}
