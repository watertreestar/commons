package com.young.commons.function;

@FunctionalInterface
public interface CheckedSupplier<R> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws Throwable if an error occurs
     */
    R get() throws Throwable;
}
