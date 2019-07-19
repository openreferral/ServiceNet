package org.benetech.servicenet.util;

import java.util.Locale;

public final class UrlNormalizationUtils {

    private static final String WWW = "WWW.";
    private static final String HTTPS = "HTTPS://";
    private static final String HTTP = "HTTP://";
    private static final String TRAILING = "/";

    public static String normalize(String url) {
        if (url == null) {
            return null;
        }
        String result = url.replaceAll(" ", "");

        if (result.toUpperCase(Locale.ROOT).startsWith(HTTP)) {
            result = result.substring(HTTP.length());
        }
        if (result.toUpperCase(Locale.ROOT).startsWith(HTTPS)) {
            result = result.substring(HTTPS.length());
        }
        if (result.toUpperCase(Locale.ROOT).startsWith(WWW)) {
            result = result.substring(WWW.length());
        }
        if (result.toUpperCase(Locale.ROOT).endsWith(TRAILING)) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    
    private UrlNormalizationUtils() {
    }
}
