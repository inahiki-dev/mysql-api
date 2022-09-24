package inahiki.develop.builder;

import inahiki.develop.exception.ParentCheckBuilderException;
import inahiki.develop.utils.Utils;
import inahiki.develop.builder.into.values.Values;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheckBuilder<T> {

    private final List<String> lines = new ArrayList<>();

    private AbstractCheckBuilder<T> parent;

    public AbstractCheckBuilder() {}

    private AbstractCheckBuilder(AbstractCheckBuilder<T> parent) {
        this.parent = parent;
    }

    public AbstractCheckBuilder<T> up() {
        return new AbstractCheckBuilder<T>(this) {

            @Override
            public T add() throws ParentCheckBuilderException {
                checkParent("This constructor is not the main one");
                return null;
            }

        };
    }

    private void checkParent(String message) throws ParentCheckBuilderException {
        if (parent == null) throw new ParentCheckBuilderException(message);
    }

    public AbstractCheckBuilder<T> down() throws ParentCheckBuilderException {
        checkParent("This builder is the main");
        return parent.append("(" + this + ")");
    }

    public AbstractCheckBuilder<T> append(String line) {
        lines.add(line);
        return this;
    }

    public AbstractCheckBuilder<T> append(String column, String compare, Object value) {
        return append(column).append(compare).append(Values.format(value));
    }

    public AbstractCheckBuilder<T> like(String column, Object value) {
        return append(column, "LIKE", value);
    }

    public AbstractCheckBuilder<T> greater(String column, Object value) {
        return append(column, ">", value);
    }

    public AbstractCheckBuilder<T> less(String column, Object value) {
        return append(column, "<", value);
    }

    public AbstractCheckBuilder<T> is(String column, Object value) {
        return append(column, "IS", value);
    }

    public AbstractCheckBuilder<T> equal(String column, Object value) {
        return append(column, "=", value);
    }

    public AbstractCheckBuilder<T> greaterEqual(String column, Object value) {
        return append(column, ">=", value);
    }

    public AbstractCheckBuilder<T> lessEqual(String column, Object value) {
        return append(column, "<=", value);
    }

    public AbstractCheckBuilder<T> and() {
        return append("AND");
    }

    public AbstractCheckBuilder<T> or() {
        return append("OR");
    }

    @Override
    public String toString() {
        return Utils.join(lines);
    }

    public abstract T add() throws ParentCheckBuilderException;
}

