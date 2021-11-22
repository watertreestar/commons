package com.young.commons.conversion;

import java.net.InetSocketAddress;

public class Conversions {
    public static String toStringOrNull(Object object) {
        return toStringOrDefault(object, null);
    }

    public static String toStringOrDefault(Object object, String defaultString) {
        return object == null ? defaultString : object.toString();
    }

    public static Long toLongOrNull(String aString) {
        return toLongOrDefault(aString, null);
    }

    public static Long toLongOrDefault(String aString, Long defaultValue) {
        return aString == null ? defaultValue : Long.valueOf(aString);
    }

    public static String toHostAndPortAsString(InetSocketAddress address) {
        return String.format("%s:%d", address.getHostName(), address.getPort());
    }

    public static boolean stringIsEmpty(String aString) {
        return aString == null || aString.isEmpty();
    }
}
