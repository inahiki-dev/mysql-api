package inahiki.develop.builder.table;

import inahiki.develop.enums.IndexType;

public class TableKeyBuilder {

    private final TableBuilder tableBuilder;
    private final String column;

    private IndexType type;

    public TableKeyBuilder(TableBuilder tableBuilder, String column) {
        this.tableBuilder = tableBuilder;
        this.column = column;
    }

    public TableKeyBuilder using(IndexType type) {
        this.type = type;
        return this;
    }

    public TableBuilder add() {
        String line = "PRIMARY KEY (" + column + ")";
        if (type != null) line += " USING " + type.name();
        return tableBuilder.addUnsafeColumn(line);
    }
}
