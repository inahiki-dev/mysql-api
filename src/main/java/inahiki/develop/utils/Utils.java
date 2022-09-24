package inahiki.develop.utils;

import java.util.Locale;

public class Utils {

    public static String constraint(String constraint, String table, String superLine) {
        String line = "";
        if (constraint != null) {
            line = "CONSTRAINT ";
            if (constraint.isEmpty()) {
                String temp = table + "_chk_" + superLine.hashCode() % 1000;
                line += temp.toLowerCase(Locale.ROOT);
            } else {
                line += constraint;
            }
            line += " ";
        }
        return line;
    }

    public static String join(Iterable<? extends CharSequence> columns) {
        return String.join(" ", columns);
    }

    public static String join(CharSequence... columns) {
        return String.join(" ", columns);
    }

    public static String joinSet(Iterable<? extends CharSequence> columns) {
        return String.join(", ", columns);
    }

    public static String joinSet(CharSequence... columns) {
        return String.join(", ", columns);
    }
}
