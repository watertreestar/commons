package com.young.commons.function;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class MemoizedFunction<T, R> implements Function<T, R> {
    private final Function<T, R> function;
    private final ConcurrentMap<T, R> map = new ConcurrentHashMap<>();

    public MemoizedFunction(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public R apply(T t) {
        requireNonNull(t);
        // this map would not remove elements, so we can eliminate lock cost when element exists by getting the element
        // before call computeIfAbsent
        R value = map.get(t);
        if (value != null) {
            return value;
        }
        value = map.computeIfAbsent(t, function);
        requireNonNull(value);
        return value;
    }
}
