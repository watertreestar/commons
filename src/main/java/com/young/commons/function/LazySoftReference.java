package com.young.commons.function;

import java.lang.ref.SoftReference;
import java.util.function.Supplier;

public class LazySoftReference<T> {
    private final Supplier<T> initializer;
    private SoftReference<T> softRef = null;

    private LazySoftReference(final Supplier<T> initializer) {
        this.initializer = initializer;
    }

    public static <T> LazySoftReference<T> of(Supplier<T> initializer) {
        return new LazySoftReference<>(initializer);
    }

    public T get() {
        if (softRef == null) {
            softRef = new SoftReference<>(initializer.get());
        }
        T val = softRef.get();
        if (val == null) {
            val = initializer.get();
            softRef = new SoftReference<>(val);
        }
        return val;
    }

    /**
     * Returns true if the soft reference has been initialized.
     *
     * @return true if the soft reference has been initialized.
     */
    public boolean isInitialized() {
        return softRef != null;
    }

    /**
     * Returns true if the soft reference has been cleared by GC.
     *
     * @return true if the soft reference has been cleared.
     */
    public boolean isCleared() {
        return softRef != null && softRef.get() == null;
    }
}
