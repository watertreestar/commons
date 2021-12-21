package com.young.commons;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtils {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static byte[] getBytes(final String value) {
        return getBytes(value, DEFAULT_CHARSET);
    }

    public static byte[] getBytes(final String value, final Charset charset) {
        return value.getBytes(charset);
    }

    public static String fromBytes(final byte[] bytes) {
        return fromBytes(bytes, DEFAULT_CHARSET);
    }

    public static String fromBytes(final byte[] bytes, final Charset charset) {
        return new String(bytes, charset);
    }

    public static String limitString(final String message, final int maxLength) {
        if (message.length() > maxLength) {
            return message.substring(0, maxLength).concat("...");
        } else {
            return message;
        }
    }
}
