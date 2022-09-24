package inahiki.develop.builder.select.order;

import inahiki.develop.utils.Utils;

public class Order {

    private final String[] columns;
    private final OrderType type;

    public Order(String[] columns, OrderType type) {
        this.columns = columns;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "ORDER BY " + Utils.joinSet(columns) + (type != null ? " " + type.name() : "");
    }
}
