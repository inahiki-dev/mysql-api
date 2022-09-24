package inahiki.develop.builder.into.insert;

import inahiki.develop.builder.into.AbstractIntoBuilder;
import inahiki.develop.connection.SQLConnection;

public class InsertIntoBuilder extends AbstractIntoBuilder {

    public InsertIntoBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    @Override
    protected String into() {
        return "INSERT";
    }
}
