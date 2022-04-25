package com.young.commons;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Syncs {
    public static void sync(Future<?>... futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException e) {
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
            } catch (ExecutionException e) {
            }
        }
    }
}
