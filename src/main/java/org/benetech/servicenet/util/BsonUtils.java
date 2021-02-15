package org.benetech.servicenet.util;

import java.nio.charset.StandardCharsets;

public class BsonUtils {

    private BsonUtils() {}
    public static String docToString(Object doc) {
        if (doc instanceof byte[]) {
            return new String((byte[]) doc, StandardCharsets.UTF_8);
        } else {
            return (String) doc;
        }
    }

    public static byte[] docToByteArray(Object doc) {
        if (doc instanceof byte[]) {
            return (byte[]) doc;
        } else {
            return ((String) doc).getBytes(StandardCharsets.UTF_8);
        }
    }

}
