package com.young.commons.data;

import java.io.Serializable;

public class Triple<A, B, C> implements Serializable {
    private static final long serialVersionUID = -6791546755492843197L;
    private final A first;
    private final B second;
    private final C third;

    private Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Create new Triple. The values cannot be null
     */
    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }

    /**
     * Create one new tuple, replace first value with new value.
     */
    public Triple<A, B, C> withFirst(A first) {
        return new Triple<>(first, second, third);
    }

    /**
     * Create one new tuple, replace second value with new value.
     */
    public Triple<A, B, C> withSecond(B second) {
        return new Triple<>(first, second, third);
    }

    /**
     * Create one new tuple, replace second value with new value.
     */
    public Triple<A, B, C> withThrid(C third) {
        return new Triple<>(first, second, third);
    }

    /**
     * The first value of this tuple
     */
    public A first() {
        return first;
    }

    /**
     * The second value of this tuple
     */
    public B second() {
        return second;
    }

    /**
     * The third value of this tuple
     */
    public C third() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;

        if (first != null ? !first.equals(triple.first) : triple.first != null) return false;
        if (second != null ? !second.equals(triple.second) : triple.second != null) return false;
        return third != null ? third.equals(triple.third) : triple.third == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (third != null ? third.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ')';
    }
}
