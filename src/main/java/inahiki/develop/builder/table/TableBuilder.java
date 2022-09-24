package inahiki.develop.builder.table;

import inahiki.develop.builder.AbstractCheckBuilder;
import inahiki.develop.builder.AbstractColumnBuilder;
import inahiki.develop.builder.AbstractRequestExecutor;
import inahiki.develop.connection.SQLConnection;
import inahiki.develop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder extends AbstractRequestExecutor {

    private final List<String> addons = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();

    private boolean if_not_exists = false;

    public TableBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    public TableBuilder ifNotExists() {
        if_not_exists = true;
        return this;
    }

    public TableBuilder ifNotExists(boolean if_not_exists) {
        this.if_not_exists = if_not_exists;
        return this;
    }

    public AbstractColumnBuilder<TableBuilder> column(String column) {
        return new AbstractColumnBuilder<TableBuilder>(column) {

            @Override
            public TableBuilder add() {
                return TableBuilder.this.addUnsafeColumn(this.toString());
            }

        };
    }

    public TableKeyBuilder primaryKey(String column) {
        return new TableKeyBuilder(this, column);
    }

    public TableForeignBuilder foreignKey(String column) {
        return new TableForeignBuilder(this, column);
    }

    public AbstractCheckBuilder<TableBuilder> check() {
        return check("");
    }

    public AbstractCheckBuilder<TableBuilder> check(String constraint) {
        return new AbstractCheckBuilder<TableBuilder>() {

            @Override
            public TableBuilder add() {
                String superLine = super.toString();
                String constraintPrefix = Utils.constraint(constraint, getTable(), superLine);
                return addUnsafeColumn(constraintPrefix + "CHECK (" + superLine + ")");
            }

        };
    }

    protected TableBuilder addUnsafeColumn(String line) {
        columns.add(line);
        return this;
    }

    @Override
    public String toSQLLine() {
        return String.format(
                "CREATE TABLE%s %s (%s)%s",
                if_not_exists ? " IF NOT EXISTS" : "",
                getTable(),
                Utils.joinSet(columns),
                Utils.join(addons)
        );
    }
}
