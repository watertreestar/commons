package com.young.commons.file;

import java.util.Locale;

public class Files {
    private static final String[] SI_UNITS = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};
    private static final String[] BINARY_UNITS = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /**
     * @param bytes
     * @param useSIUnits
     * @param locale
     * @return
     */
    public static String humanReadableByteCount(final long bytes, final boolean useSIUnits, final Locale locale) {
        final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
        final int base = useSIUnits ? 1000 : 1024;

        // When using the smallest unit no decimal point is needed, because it's the exact number.
        if (bytes < base) {
            return bytes + " " + units[0];
        }

        final int exponent = (int) (Math.log(bytes) / Math.log(base));
        final String unit = units[exponent];
        return String.format(locale, "%.1f %s", bytes / Math.pow(base, exponent), unit);
    }

    public static String humanReadableByteCount(final long bytes, boolean useSIUnits) {
        return humanReadableByteCount(bytes, useSIUnits, Locale.getDefault());
    }
}
