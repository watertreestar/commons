package com.young.commons.thread;

public class Threads {
    public static long sleepFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        return milliseconds;
    }
}
