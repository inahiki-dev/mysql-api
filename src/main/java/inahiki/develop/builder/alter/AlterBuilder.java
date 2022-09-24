package inahiki.develop.builder.alter;

import inahiki.develop.builder.AbstractCheckBuilder;
import inahiki.develop.builder.AbstractColumnBuilder;
import inahiki.develop.builder.AbstractRequestExecutor;
import inahiki.develop.connection.SQLConnection;
import inahiki.develop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AlterBuilder extends AbstractRequestExecutor {

    private final List<String> alterOptions = new ArrayList<>();

    public AlterBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    public AbstractColumnBuilder<AlterBuilder> addFirstColumn(String column) {
        return addUnsafeColumn(column, " FIRST");
    }

    public AbstractColumnBuilder<AlterBuilder> addColumn(String column, String afterColumn) {
        return addUnsafeColumn(column, " AFTER " + afterColumn);
    }

    public AbstractColumnBuilder<AlterBuilder> addColumn(String column) {
        return addUnsafeColumn(column, "");
    }

    private AbstractColumnBuilder<AlterBuilder> addUnsafeColumn(String column, String extra) {
        return unsafeColumn("ADD", column, extra);
    }

    public AbstractColumnBuilder<AlterBuilder> modifyFirstColumn(String column) {
        return modifyUnsafeColumn(column, " FIRST");
    }

    public AbstractColumnBuilder<AlterBuilder> modifyColumn(String column, String afterColumn) {
        return modifyUnsafeColumn(column, " AFTER " + afterColumn);
    }

    public AbstractColumnBuilder<AlterBuilder> modifyColumn(String column) {
        return modifyUnsafeColumn(column, "");
    }

    private AbstractColumnBuilder<AlterBuilder> modifyUnsafeColumn(String column, String extra) {
        return unsafeColumn("MODIFY", column, extra);
    }

    private AbstractColumnBuilder<AlterBuilder> unsafeColumn(String option, String column, String extra) {
        return new AbstractColumnBuilder<AlterBuilder>(column) {

            @Override
            public AlterBuilder add() {
                return addUnsafeOption(option + " COLUMN " + this + extra);
            }

        };
    }

    public AbstractCheckBuilder<AlterBuilder> addCheck() {
        return addCheck(null);
    }

    public AbstractCheckBuilder<AlterBuilder> addCheck(String constraint) {
        return new AbstractCheckBuilder<AlterBuilder>() {

            @Override
            public AlterBuilder add() {
                String superLine = super.toString();
                String constraintPrefix = Utils.constraint(constraint, getTable(), superLine);
                return addUnsafeOption("ADD " + constraintPrefix + "CHECK (" + superLine + ")");
            }

        };
    }

    public AlterBuilder dropColumn(String column) {
        return addUnsafeOption("DROP COLUMN " + column);
    }

    public AlterBuilder dropPrimaryKey() {
        return addUnsafeOption("DROP PRIMARY KEY");
    }

    public AlterBuilder dropForeignKey(String fk_symbol) {
        return addUnsafeOption("DROP FOREIGN KEY " + fk_symbol);
    }

    protected AlterBuilder addUnsafeOption(String line) {
        alterOptions.add(line);
        return this;
    }

    @Override
    public String toSQLLine() {
        return String.format(
                "ALTER TABLE %s %s",
                getTable(),
                Utils.joinSet(alterOptions)
        );
    }
}
