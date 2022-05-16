package com.young.commons.lifecycle;

public interface LifeCycle {
    void start() throws Throwable;

    void shutdown() throws Throwable;
}
