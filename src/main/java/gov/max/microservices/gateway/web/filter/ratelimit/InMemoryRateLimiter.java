package gov.max.microservices.gateway.web.filter.ratelimit;

import java.util.WeakHashMap;

/**
 * Loosely based on a Leaky Bucket algorithm: https://en.wikipedia.org/wiki/Leaky_bucket
 *
 * Use RedisRateLimiter for production uses.
 */
public class InMemoryRateLimiter implements RateLimiter {

    WeakHashMap<String, Rate> cache = new WeakHashMap<>();


    @Override
    public synchronized Rate consume(Policy policy, String key) {
        Rate rate = cache.get(key);

        if (rate == null) {
            rate = create(policy, key);
        }
        replenish(policy, rate);
        return Rate.from(rate);
    }

    private void replenish(Policy policy, Rate rate) {
        if (rate.getReset() <= System.currentTimeMillis()) {
            rate.setRemaining(policy.getLimit());
            rate.setReset(System.currentTimeMillis() + policy.getRefreshInterval() * 1000);
        }
        rate.setRemaining(rate.getRemaining() - 1);
    }

    private Rate create(Policy policy, String key) {
        Rate rate = new Rate(policy.getLimit(), policy.getLimit(), (System.currentTimeMillis() + policy.getRefreshInterval() * 1000));
        cache.put(key, rate);
        return rate;
    }

}
