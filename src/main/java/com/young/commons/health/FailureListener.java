package com.young.commons.health;

/**
 * Failure Listener invoked by a {@link HealthMonitorable} component.
 */
public interface FailureListener {

    /**
     * Invoked when the health status becomes unhealthy.
     */
    void onFailure();

    /**
     * Invoked when health status becomes healthy after being unhealthy for some time. A component can
     * be marked unhealthy initially and set to healthy only after start up is complete. It is
     * expected to call {#onRecovered} when it is marked as healthy.
     */
    void onRecovered();

    /**
     * Invoked when the health status becomes dead and the system can't become healthy again without
     * external intervention.
     */
    void onUnrecoverableFailure();
}
