package org.benetech.servicenet.adapter;

import java.io.IOException;
import java.io.InputStream;

public final class AdapterTestsUtils {

    private static final String RESOURCE_PACKAGE = "adapters/";

    public static String readResourceAsString(String resourceName) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(RESOURCE_PACKAGE + resourceName)) {
            return new String(is.readAllBytes());
        }
    }

    private AdapterTestsUtils() {
    }
}
