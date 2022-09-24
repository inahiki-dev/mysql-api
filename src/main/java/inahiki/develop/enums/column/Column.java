package inahiki.develop.enums.column;

public enum Column {

    // ------------------------------------------------
    // 1 Numeric Data Types
    // ------------------------------------------------

    // 1.2 Integer Types (Exact Value)
    INTEGER, INT, SMALLINT, TINYINT, MEDIUMINT, BIGINT,

    // 1.3 Fixed-Point Types (Exact Value)
    DECIMAL, NUMERIC,

    // 1.4 Floating-Point Types (Approximate Value)
    FLOAT, DOUBLE,

    // 1.5 Bit-Value Type
    BIT,

    // ------------------------------------------------
    // 2 Date and Time Data Types
    // ------------------------------------------------

    // 2.2 The DATE, DATETIME, and TIMESTAMP Types
    DATE, DATETIME, TIMESTAMP,

    // 2.3 The TIME Type
    TIME,

    // 2.4 The YEAR Type
    YEAR,

    // ------------------------------------------------
    // 3 String Data Types
    // ------------------------------------------------

    // 3.2 The CHAR and VARCHAR Types
    CHAR, VARCHAR,

    // 3.3 The BINARY and VARBINARY Types
    BINARY, VARBINARY,

    // 3.4 The BLOB and TEXT Types
    BLOB, TEXT,

    // 3.5 The ENUM Type
    ENUM,

    // 3.6 The SET Type
    SET;
}
