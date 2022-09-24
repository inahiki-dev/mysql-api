package inahiki.develop.exception;

import java.sql.SQLException;

public class SQLCException extends Exception {

    public SQLCException(Exception e) {
        super(e);
    }
}
