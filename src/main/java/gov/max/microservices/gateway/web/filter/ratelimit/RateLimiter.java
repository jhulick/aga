package gov.max.microservices.gateway.web.filter.ratelimit;

public interface RateLimiter {

    /**
     *
     * @param policy - Template for which rates should be created in case there's no rate limit associated with the key
     * @param key - Unique key that identifies a request
     * @return a view of a user's rate request limit
     */
    public Rate consume(Policy policy, String key);
}
