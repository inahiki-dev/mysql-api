package inahiki.develop.builder;

import inahiki.develop.enums.column.Column;
import inahiki.develop.builder.into.values.Values;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractColumnBuilder<T> {

    protected final String column;

    private Column type;
    private Object[] type_params;
    private String def;

    private boolean notNull = false;
    private boolean autoInc = false;
    private boolean unique = false;
    private boolean primary = false;

    public AbstractColumnBuilder(String column) {
        this.column = column;
    }

    public AbstractColumnBuilder<T> type(Column type) {
        this.type = type;
        return this;
    }

    public AbstractColumnBuilder<T> type(Column type, Object... type_params) {
        this.type_params = type_params;
        return type(type);
    }

    public AbstractColumnBuilder<T> notNull() {
        notNull = true;
        return this;
    }

    public AbstractColumnBuilder<T> autoInc() {
        autoInc = true;
        return this;
    }

    public AbstractColumnBuilder<T> unique() {
        unique = true;
        return this;
    }

    public AbstractColumnBuilder<T> primary() {
        primary = true;
        return this;
    }

    public AbstractColumnBuilder<T> def(Object value) {
        def = Values.format(value);
        return this;
    }

    abstract public T add();

    @Override
    public String toString() {
        String line = column + " " + type.name();
        if (type_params != null) {
            String paramsLine = Stream.of(type_params)
                    .map(Values::format)
                    .collect(Collectors.joining(", "));
            line += "(" + paramsLine + ")";
        }
        line += notNull ? " NOT NULL" : " NULL";
        if (def != null) {
            line += " DEFAULT " + def;
        }
        if (autoInc) line += " AUTO_INCREMENT";
        if (unique) line += " UNIQUE";
        if (primary) line += " PRIMARY KEY";
        return line;
    }

}
