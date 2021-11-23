package com.young.commons.file;

import org.junit.Test;

import java.util.Locale;

public class FilesTest {

    @Test
    public void testDefaultLocal() {
        String size = Files.humanReadableByteCount(1024 * 12, false);
        System.out.println(size);
    }

    @Test
    public void testWithLocal() {
        String size = Files.humanReadableByteCount(1024 * 12, false, Locale.ENGLISH);
        System.out.println(size);
    }
}
