package com.young.commons.function;

import java.util.function.Function;

public class Functions {
    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        return new MemoizedFunction<T, R>(function);
    }
}
