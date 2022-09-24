package inahiki.develop.builder.into.replace;

import inahiki.develop.builder.into.AbstractIntoBuilder;
import inahiki.develop.connection.SQLConnection;

public class ReplaceIntoBuilder extends AbstractIntoBuilder {

    public ReplaceIntoBuilder(SQLConnection connection, String table) {
        super(connection, table);
    }

    @Override
    protected String into() {
        return "REPLACE";
    }

}
