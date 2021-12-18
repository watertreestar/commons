package com.young.commons;

public interface LifeCycle {
    void start() throws Throwable;

    void shutdown() throws Throwable;
}
