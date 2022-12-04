package inahiki.develop.connection;

import inahiki.develop.builder.alter.AlterBuilder;
import inahiki.develop.builder.delete.DeleteBuilder;
import inahiki.develop.builder.into.insert.InsertIntoBuilder;
import inahiki.develop.builder.into.replace.ReplaceIntoBuilder;
import inahiki.develop.builder.select.SelectBuilder;
import inahiki.develop.builder.table.TableBuilder;
import inahiki.develop.exception.ParamLostException;
import inahiki.develop.exception.SQLCException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLConnection {

    // ---------------------------------------------------------------
    //  Static methods
    // ---------------------------------------------------------------

    public static SQLConnection of(Map<?, ?> settings, boolean addDefaultParams) throws ParamLostException {
        Map<?, ?> tempSettings = new HashMap<>(settings);
        SQLCBuilder builder = new SQLCBuilder(addDefaultParams)
                .host(tempSettings.remove("host").toString())
                .port(tempSettings.remove("port").toString())
                .user(tempSettings.remove("user").toString())
                .password(tempSettings.remove("password").toString())
                .database(tempSettings.remove("database").toString());
        tempSettings.forEach((key, value) -> builder.addParam(key.toString(), value));
        return builder.create();
    }

    public static SQLConnection of(Map<?, ?> settings) throws ParamLostException {
        return of(settings, true);
    }

    public static SQLCBuilder builder(boolean addDefaultParams) {
        return new SQLCBuilder(addDefaultParams);
    }

    public static SQLCBuilder builder() {
        return new SQLCBuilder(true);
    }

    // ---------------------------------------------------------------
    //  SQLConnection object
    // ---------------------------------------------------------------

    private final String URL;

    private Connection connection;
    private Statement statement;

    protected SQLConnection(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public boolean isClosed() throws SQLException {
        return connection == null || connection.isClosed();
    }

    public void open() throws SQLCException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            throw new SQLCException(e);
        }
    }

    public void close() throws SQLException {
        if (statement != null && !statement.isClosed()) {
            statement.close();
            statement = null;
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public void execute(String request) throws SQLCException {
        try {
            try {
                statement.execute(request);
            } catch (Exception e) {
                if (e.getClass().getSimpleName().equals("CommunicationsException")) {
                    reconnect();
                    execute(request);
                } else {
                    throw e;
                }
            }
        } catch (SQLException e) {
            throw new SQLCException(e);
        }
    }

    public ResultSet executeQuery(String request)  throws SQLCException {
        try {
            try {
                return statement.executeQuery(request);
            } catch (Exception e) {
                if (e.getClass().getSimpleName().equals("CommunicationsException")) {
                    reconnect();
                    return executeQuery(request);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new SQLCException(e);
        }
    }

    private void reconnect() throws SQLCException {
        try {
            close();
            open();
        } catch (SQLException e) {
            throw new SQLCException(e);
        }
    }

    // ---------------------------------------------------------------
    //  Request builders
    // ---------------------------------------------------------------

    public TableBuilder createTable(String table) {
        return new TableBuilder(this, table);
    }

    public AlterBuilder alterTable(String table) {
        return new AlterBuilder(this, table);
    }

    public SelectBuilder select() {
        return new SelectBuilder(this);
    }

    public ReplaceIntoBuilder replaceInto(String table) {
        return new ReplaceIntoBuilder(this, table);
    }

    public InsertIntoBuilder insertInto(String table) {
        return new InsertIntoBuilder(this, table);
    }

    public DeleteBuilder delete(String table) {
        return new DeleteBuilder(this, table);
    }

}
