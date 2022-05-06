package com.young.commons;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Syncs {
    public static void sync(Future<?>... futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException ignored) {
            }
        }
    }

    /**
     * wait all futures finished
     */
    public static void sync(Collection<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException ignored) {
            }
        }
    }

    public static <T> T sync(Future<T> f) {
        try {
            return f.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T sync(final Future<T> f, final long timeout, final TimeUnit timeUnit) {
        try {
            return f.get(timeout, timeUnit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
