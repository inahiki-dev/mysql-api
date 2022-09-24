package inahiki.develop.exception;

import java.util.List;

public class ParamLostException extends Exception {

    private final List<String> params;

    public ParamLostException(List<String> params) {
        super("Lost parameters: \"" + String.join("\", \"", params) + "\"");
        this.params = params;
    }

    public List<String> getParams() {
        return params;
    }
}
