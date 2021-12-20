package com.young.commons.thread;

import java.util.concurrent.ExecutorService;

public interface MonitoringExecutorService extends ExecutorService {
    /**
     * @return the approximate wait task count of the executor.
     */
    int waitCount();

    /**
     * @return the approximate executing count of the executor.
     */
    int executingCount();

    /**
     * @return the approximate complete count of the executor.
     */
    long completeCount();
}
