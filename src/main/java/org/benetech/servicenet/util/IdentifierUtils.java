package org.benetech.servicenet.util;

public class IdentifierUtils {

    public static String toBase36(Integer id) {
        String base36 = Integer.toString(id, 36);
        // pad with zeros)
        return String.format("%1$4s", base36).replace(' ', '0');
    }

    public static Integer toInteger(String paddedBase36) {
        return Integer.valueOf(paddedBase36, 36);
    }

    private IdentifierUtils() { }
}
