package inahiki.develop.builder;

import inahiki.develop.connection.SQLConnection;
import inahiki.develop.exception.SQLCException;

public abstract class AbstractRequestExecutor {

    private final SQLConnection connection;
    private final String table;

    public AbstractRequestExecutor(SQLConnection connection, String  table) {
        this.connection = connection;
        this.table = table;
    }

    public abstract String toSQLLine();

    public SQLConnection getConnection() {
        return connection;
    }

    public String getTable() {
        return table;
    }

    public void execute() throws SQLCException {
        executeAsString();
    }

    public String executeAsString() throws SQLCException {
        String request = toSQLLine();
        getConnection().execute(request);
        return request;
    }

    @Override
    public String toString() {
        return toSQLLine();
    }
}
