package inahiki.develop.builder.delete;

import inahiki.develop.builder.AbstractCheckBuilder;
import inahiki.develop.builder.AbstractRequestExecutor;
import inahiki.develop.builder.select.SelectBuilder;
import inahiki.develop.builder.select.order.Order;
import inahiki.develop.builder.select.order.OrderType;
import inahiki.develop.connection.SQLConnection;
import inahiki.develop.utils.Utils;

import java.util.LinkedList;
import java.util.List;

public class DeleteBuilder extends AbstractRequestExecutor {

    private boolean ignore = false;

    private String where;
    private Integer limit;
    private Order order;

    public DeleteBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    public AbstractCheckBuilder<DeleteBuilder> where() {
        return new AbstractCheckBuilder<DeleteBuilder>() {

            @Override
            public DeleteBuilder add() {
                where = toString();
                return DeleteBuilder.this;
            }

        };
    }

    public DeleteBuilder ignore() {
        ignore = true;
        return this;
    }

    public DeleteBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public DeleteBuilder orderBy(String... columns) {
        return orderBy(null, columns);
    }

    public DeleteBuilder orderBy(OrderType type, String... columns) {
        order = new Order(columns, type);
        return this;
    }

    @Override
    public String toSQLLine() {
        List<String> extraLines = new LinkedList<>();
        if (where != null) extraLines.add("WHERE " + where);
        if (order != null) extraLines.add(order.toString());
        if (limit != null) extraLines.add("LIMIT " + limit);
        return String.format(
                "DELETE %sFROM %s%s",
                ignore ? "IGNORE " : "",
                getTable(),
                extraLines.isEmpty() ? "" : " " + Utils.join(extraLines)
        );
    }
}
