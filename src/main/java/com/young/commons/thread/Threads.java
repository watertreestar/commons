package com.young.commons.thread;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Threads {
    public static long sleepMills(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        return milliseconds;
    }

    public static void sleepDuration(Duration duration) {
        if (duration.compareTo(Duration.ZERO) < 0) {
            throw new IllegalArgumentException("timeout value is negative: " + duration);
        }
        sleepNanos(duration.toNanos());
    }

    public static void sleepNanos(long nanos) {
        if (nanos < 0) {
            throw new IllegalArgumentException("timeout value is negative: " + nanos);
        }
        if (nanos == 0) {
            return;
        }
        long untilNanos = System.nanoTime() + nanos;
        long toSleepNanos = nanos;
        boolean interrupted = false;
        do {
            try {
                TimeUnit.NANOSECONDS.sleep(toSleepNanos);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            toSleepNanos = untilNanos - System.nanoTime();

        } while (toSleepNanos > 0);
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Thread thread = new Thread(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        thread.start();
        return future;
    }

    /**
     * Start a thread, run task async, and return future contains the result. When task finished, the thread exits.
     */
    public static <T> CompletableFuture<T> callAsync(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread thread = new Thread(() -> {
            try {
                T result = callable.call();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        thread.start();
        return future;
    }
}
