package inahiki.develop.builder.table;

import inahiki.develop.enums.Action;

import java.util.Locale;

public class TableForeignBuilder {

    private final TableBuilder tableBuilder;
    private final String column;

    private String tableReferences;
    private String keyReferences;
    private String constraint;
    private Action onDelete;
    private Action onUpdate;

    public TableForeignBuilder(TableBuilder tableBuilder, String column) {
        this.tableBuilder = tableBuilder;
        this.column = column;
    }

    public TableForeignBuilder references(String table, String columnKey) {
        this.tableReferences = table;
        this.keyReferences = columnKey;
        return this;
    }

    public TableForeignBuilder constraint() {
        return constraint("");
    }

    public TableForeignBuilder constraint(String constraint) {
        this.constraint = constraint;
        return this;
    }

    public TableForeignBuilder onDelete(Action action) {
        this.onDelete = action;
        return this;
    }

    public TableForeignBuilder onUpdate(Action action) {
        this.onUpdate = action;
        return this;
    }

    public TableBuilder add() {
        String line = "";
        if (constraint != null) {
            line += "CONSTRAINT ";
            if (constraint.isEmpty()) {
                String ct = tableBuilder.getTable() + "_" + tableReferences + "_" + keyReferences + "_fk";
                line += ct.toLowerCase(Locale.ROOT);
            } else {
                line += constraint;
            }
            line += ' ';
        }
        line += "FOREIGN KEY (" + column + ") REFERENCES " + tableReferences + " (" + keyReferences + ")";
        if (onDelete != null) line += " ON DELETE " + onDelete.option();
        if (onUpdate != null) line += " ON UPDATE " + onUpdate.option();
        return tableBuilder.addUnsafeColumn(line);
    }
}
