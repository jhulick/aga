package gov.max.microservices.gateway.web.filter.ratelimit;

/**
 * A policy is used to define rate limit constraints within RateLimiter implementations
 */
public class Policy {

    private Long refreshInterval;

    private Long limit;

    public Policy() {
    }

    public Policy(Long refreshInterval, Long limit) {
        this.refreshInterval = refreshInterval;
        this.limit = limit;
    }

    public static enum PolicyType {
        ANONYMOUS("anonymous"), AUTHENTICATED("authenticated");
        private final String type;
        PolicyType(String type) {
            this.type = type;
        }
    }

    public Long getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(Long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
