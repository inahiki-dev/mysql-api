package inahiki.develop.connection;

import inahiki.develop.exception.ParamLostException;
import inahiki.develop.exception.SQLCException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SQLCBuilder {

    private final Map<String, String> params = new HashMap<>();

    private String port;
    private String host;
    private String database;

    protected SQLCBuilder(boolean addDefaultParams) {
        if (addDefaultParams) {
            addParam("initialTimeout", 1);
            addParam("useSSL", false);
            addParam("useUnicode", true);
            addParam("characterEncoding", "UTF-8");
            port = "3306";
        }
    }

    public SQLCBuilder host(String host) {
        this.host = host;
        return this;
    }

    public SQLCBuilder port(String port) {
        this.port = port;
        return this;
    }

    public SQLCBuilder database(String database) {
        this.database = database;
        return this;
    }

    public SQLCBuilder user(String user) {
        return addUnsafeParam("user", user);
    }

    public SQLCBuilder password(String password) {
        return addParamWithEncode("password", password);
    }

    public SQLCBuilder initialTimeout(int initialTimeout) {
        return addParam("initialTimeout", initialTimeout);
    }

    public SQLCBuilder useSSL(boolean useSSL) {
        return addParam("useSSL", useSSL);
    }

    public SQLCBuilder useUnicode(boolean useUnicode) {
        return addParam("useUnicode", useUnicode);
    }

    public SQLCBuilder characterEncoding(int characterEncoding) {
        return addParam("characterEncoding", characterEncoding);
    }

    public SQLCBuilder clearParams() {
        params.clear();
        return this;
    }

    public SQLCBuilder addParam(String key, Object value) {
        return addUnsafeParam(key, value.toString());
    }

    public SQLCBuilder addParamWithEncode(String key, Object value) {
        try {
            return addUnsafeParam(key, URLEncoder.encode(value.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return this;
        }
    }

    private SQLCBuilder addUnsafeParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public SQLConnection createAndOpen() throws ParamLostException, SQLCException {
        SQLConnection connection = create();
        connection.open();
        return connection;
    }

    public SQLConnection create() throws ParamLostException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        List<String> lostParams = new ArrayList<>(Arrays.asList("host", "port", "database", "user", "password"));
        if (host != null) lostParams.remove("host");
        if (port != null) lostParams.remove("port");
        if (database != null) lostParams.remove("database");
        if (!params.isEmpty()) {
            if (params.containsKey("user")) lostParams.remove("user");
            if (params.containsKey("password")) lostParams.remove("password");
            if (!lostParams.isEmpty()) {
                throw new ParamLostException(lostParams);
            }
            url += "?" + params.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
        } else {
            throw new ParamLostException(lostParams);
        }
        return new SQLConnection(url);
    }

}
