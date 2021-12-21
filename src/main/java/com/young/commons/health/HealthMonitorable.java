package com.young.commons.health;

public interface HealthMonitorable {
    /**
     * Used by a HealthMonitor to get the health status of this component, typically invoked
     * periodically.
     *
     * @return health status
     */
    HealthStatus getHealthStatus();

    /**
     * Register a failure observer.
     *
     * @param failureListener failure observer to be invoked when a failure that affects the health
     *                        status of this component occurs
     */
    void addFailureListener(FailureListener failureListener);

    /**
     * Removes a previously registered listener. Should do nothing if it was not previously
     * registered.
     *
     * @param failureListener the failure listener to remove
     */
    void removeFailureListener(FailureListener failureListener);
}
