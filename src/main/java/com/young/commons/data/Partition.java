package com.young.commons.data;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Partition<R> implements Serializable {
    private final R matched;
    private final R missed;

    public Partition(R matched, R missed) {
        this.matched = requireNonNull(matched);
        this.missed = requireNonNull(missed);
    }

    /**
     * The result reduced by elements pass the partition predicate
     */
    public R matched() {
        return matched;
    }

    /**
     * The result reduced by elements miss the partition predicate
     */

    public R missed() {
        return missed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partition<?> that = (Partition<?>) o;
        return matched.equals(that.matched) &&
            missed.equals(that.missed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matched, missed);
    }

    @Override
    public String toString() {
        return "PartitionResult{" +
            "matched=" + matched +
            ", missed=" + missed +
            '}';
    }
}
