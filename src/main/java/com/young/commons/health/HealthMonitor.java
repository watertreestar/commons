package com.young.commons.health;

/**
 * HealthMonitor monitor all component,  calculates aggregate health
 */
public interface HealthMonitor extends HealthMonitorable {
    /**
     * Starts necessary services for monitoring. Typically implemented by a monitor to start periodic
     * monitoring.
     */
    void startMonitoring();

    /**
     * Add a component name to be monitored. The component will be marked not healthy until the
     * component is registered using {@link #registerComponent(String, HealthMonitorable)}
     */
    void monitorComponent(String componentName);

    /**
     * Stop monitoring the component.
     *
     * @param componentName
     */
    void removeComponent(String componentName);

    /**
     * Register the component to be monitored
     *
     * @param componentName
     * @param component
     */
    void registerComponent(String componentName, HealthMonitorable component);
}
