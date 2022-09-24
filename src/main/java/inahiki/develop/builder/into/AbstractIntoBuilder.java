package inahiki.develop.builder.into;

import inahiki.develop.builder.AbstractRequestExecutor;
import inahiki.develop.builder.into.replace.ReplaceIntoBuilder;
import inahiki.develop.builder.into.values.Values;
import inahiki.develop.connection.SQLConnection;
import inahiki.develop.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractIntoBuilder extends AbstractRequestExecutor {

    private final List<Values> values = new LinkedList<>();
    private final List<String> columns = new LinkedList<>();

    public AbstractIntoBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    abstract protected String into();

    public AbstractIntoBuilder column(String column) {
        columns.add(column);
        return this;
    }

    public AbstractIntoBuilder columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public AbstractIntoBuilder value(Object... objects) {
        values.add(Values.of(objects));
        return this;
    }

    public AbstractIntoBuilder valueOriginal(Object... objects) {
        values.add(Values.ofOriginal(objects));
        return this;
    }

    @Override
    public String toSQLLine() {
        return into() + String.format(
                " INTO %s%s VALUES %s",
                getTable(),
                columns.isEmpty() ? "" : " (" + Utils.joinSet(columns) + ")",
                values.stream().map(Values::toString).collect(Collectors.joining(", "))
        );
    }
}

