package com.young.commons;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class UnsafeAccess {
    public static final Unsafe UNSAFE;
    public static final int ARRAY_BYTE_BASE_OFFSET;

    private UnsafeAccess() {}

    static {
        Unsafe unsafe = null;
        try {
            PrivilegedExceptionAction<Unsafe> action = () -> {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                return (Unsafe)f.get(null);
            };
            unsafe = AccessController.doPrivileged(action);
        } catch (Exception e) {
            try {
                throw e;
            } catch (PrivilegedActionException ex) {
                // ignore
            }
        }
        UNSAFE = unsafe;
        ARRAY_BYTE_BASE_OFFSET = Unsafe.ARRAY_BYTE_BASE_OFFSET;
    }
}
