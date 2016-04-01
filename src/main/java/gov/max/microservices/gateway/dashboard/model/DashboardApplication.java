package gov.max.microservices.gateway.dashboard.model;

import java.util.List;

/**
 * The domain model for all application at the spring cloud dashboard application.
 */
public class DashboardApplication {

    private final String name;
    private final List<Instance> instances;

    public DashboardApplication(String name, List<Instance> instances) {
        this.name = name;
        this.instances = instances;
    }

    public String getName() {
        return name;
    }

    public List<Instance> getInstances() {
        return instances;
    }
}
