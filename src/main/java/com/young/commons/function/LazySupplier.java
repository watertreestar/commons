package com.young.commons.function;

import java.util.function.Function;
import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {

    private final Supplier<? extends T> supplier;

    private T value;

    private LazySupplier(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazySupplier<T> of(Supplier<? extends T> supplier) {
        return new LazySupplier<>(supplier);
    }

    public T get() {
        if (value == null) {
            T newValue = supplier.get();
            if (newValue == null) {
                throw new IllegalStateException("LazySupplier value can not be null!");
            }
            value = newValue;
        }
        return value;
    }

    public <S> LazySupplier<S> map(Function<? super T, ? extends S> function) {
        return LazySupplier.of(() -> function.apply(get()));
    }

    public <S> LazySupplier<S> flatMap(Function<? super T, LazySupplier<? extends S>> function) {
        return LazySupplier.of(() -> function.apply(get()).get());
    }
}
