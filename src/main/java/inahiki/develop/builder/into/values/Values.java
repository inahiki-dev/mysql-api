package inahiki.develop.builder.into.values;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Values {

    public static String format(Object o) {
        return o instanceof String ? "'" + o + "'" : o.toString();
    }

    public static Values of(Object[] set) {
        return new Values(Stream.of(set).map(Values::format).toArray());
    }

    public static Values ofOriginal(Object[] set) {
        return new Values(set);
    }

    private final Object[] set;

    private Values(Object[] set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "(" + Stream.of(set).map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

}
