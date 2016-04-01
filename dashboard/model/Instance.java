package gov.max.microservices.gateway.dashboard.model;

/**
 * The domain model for all application at the spring cloud dashboard application.
 */
public class Instance {

    private final String id;
    private final String url;
    private final String name;
    private final String status;

    public Instance(String url, String name, String id, String status) {
        this.url = url.replaceFirst("/+$", "");
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
