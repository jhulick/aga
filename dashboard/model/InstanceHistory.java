package gov.max.microservices.gateway.dashboard.model;

import java.util.Date;

/**
 * The domain model for an instance history
 */
public class InstanceHistory {

    private final String id;
    private final Date date;

    public InstanceHistory(String id, Date date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
