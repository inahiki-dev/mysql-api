package inahiki.develop.builder.select;

import inahiki.develop.builder.AbstractCheckBuilder;
import inahiki.develop.builder.AbstractRequestExecutor;
import inahiki.develop.builder.select.order.Order;
import inahiki.develop.builder.select.order.OrderType;
import inahiki.develop.connection.SQLConnection;
import inahiki.develop.utils.Utils;

import java.util.*;

public class SelectBuilder extends AbstractRequestExecutor {

    private final List<String> tables = new LinkedList<>();
    private final List<String> columns = new LinkedList<>();
    private final List<String> innerJoin = new LinkedList<>();

    private String[] group;
    private String where;
    private Integer limit;
    private Order order;

    public SelectBuilder(SQLConnection connection) {
        super(connection, null);
    }

    public SelectBuilder column(String column) {
        this.columns.add(column);
        return this;
    }

    public SelectBuilder columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public SelectBuilder column(String column, String asName) {
        return column(column + " AS " + asName);
    }

    public SelectBuilder table(String table) {
        this.tables.add(table);
        return this;
    }

    public SelectBuilder tables(String... tables) {
        this.tables.addAll(Arrays.asList(tables));
        return this;
    }

    public SelectBuilder table(String table, String asName) {
        return table(table + " AS " + asName);
    }

    public AbstractCheckBuilder<SelectBuilder> where() {
        return new AbstractCheckBuilder<SelectBuilder>() {

            @Override
            public SelectBuilder add() {
                where = toString();
                return SelectBuilder.this;
            }

        };
    }

    public SelectBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectBuilder orderBy(String... columns) {
        return orderBy(null, columns);
    }

    public SelectBuilder orderBy(OrderType type, String... columns) {
        order = new Order(columns, type);
        return this;
    }

    public SelectBuilder groupBy(String... columns) {
        group = columns;
        return this;
    }

    public SelectBuilder countAll() {
        return count("*");
    }

    public SelectBuilder innerJoin(String foreignTable) {
        innerJoin.add("INNER JOIN " + foreignTable);
        return this;
    }

    public SelectBuilder innerJoin(String foreignTable, String foreignKey, String thisTable, String primaryKey) {
        innerJoin.add(
                "INNER JOIN " + foreignTable +
                " ON (" + thisTable + "." + primaryKey + " = " + foreignTable + "." + foreignKey + ")"
        );
        return this;
    }

    public SelectBuilder innerJoin(String foreignTable, String asName, String foreignKey, String thisTable, String primaryKey) {
        innerJoin.add(
                "INNER JOIN " + foreignTable + " AS " + asName +
                " ON (" + thisTable + "." + primaryKey + " = " + foreignTable + "." + foreignKey + ")"
        );
        return this;
    }

    public SelectBuilder countAllAs(String asName) {
        return column("COUNT(*)", asName);
    }

    public SelectBuilder count(String... columns) {
        return column("COUNT(" + Utils.joinSet(columns) + ")");
    }

    public SelectBuilder countAs(String asName, String... columns) {
        return column("COUNT(" + Utils.joinSet(columns) + ")", asName);
    }

    @Override
    public String getTable() {
        return Utils.joinSet(tables);
    }

    @Override
    public String toSQLLine() {
        List<String> extraLines = new LinkedList<>();
        if (where != null) extraLines.add("WHERE " + where);
        if (group != null) extraLines.add(Utils.joinSet(group));
        if (order != null) extraLines.add(order.toString());
        if (limit != null) extraLines.add("LIMIT " + limit);
        return String.format(
                "SELECT %s FROM %s%s%s",
                columns.isEmpty() ? "*" : Utils.joinSet(columns),
                getTable(),
                innerJoin.isEmpty() ? "" : " " + Utils.join(innerJoin),
                extraLines.isEmpty() ? "" : " " + Utils.join(extraLines)
        );
    }
}
