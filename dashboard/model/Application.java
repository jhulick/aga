package gov.max.microservices.gateway.dashboard.model;

import java.util.List;

/**
 * The domain model for all application at the spring cloud dashboard application.
 */
public class Application {

    private final String name;
    private final List<Instance> instances;

    public Application(String name, List<Instance> instances) {
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
