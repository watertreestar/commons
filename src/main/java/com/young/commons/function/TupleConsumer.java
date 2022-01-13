package com.young.commons.function;

public interface TupleConsumer<N, I, V> {
    void apply(N name, I index, V value);
}
