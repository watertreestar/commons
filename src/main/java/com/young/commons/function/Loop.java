package com.young.commons.function;

import java.util.Iterator;
import java.util.function.Consumer;

public class Loop {
    public static <T> void apply(Iterable<T> elements, Consumer<T> consumer) {
        elements.forEach(consumer);
    }

    public static <E> void apply(Iterator<E> elements, Consumer<E> consumer) {
        while (elements.hasNext()) {
            consumer.accept(elements.next());
        }
    }
}
