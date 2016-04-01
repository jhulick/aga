package gov.max.microservices.gateway.web.filter.ratelimit.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import gov.max.microservices.gateway.web.filter.ratelimit.Policy;
import gov.max.microservices.gateway.web.filter.ratelimit.Rate;
import gov.max.microservices.gateway.web.filter.ratelimit.RateLimiter;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class RedisRateLimiter implements RateLimiter {

    private RedisTemplate template;

    public RedisRateLimiter(RedisTemplate template) {
        this.template = template;
    }

    @Override
    public Rate consume(final Policy policy, final String key) {
        Long now = System.currentTimeMillis();
        Long time = (Long) (now / (1000 * policy.getRefreshInterval()));
        final String tempKey = key + ":" + time;
        List results = (List) template.execute(new SessionCallback() {

            @Override
            public List<Object> execute(RedisOperations ops) throws DataAccessException {
                ops.multi();
                ops.boundValueOps(tempKey).increment(1L);
                ops.expire(tempKey, policy.getRefreshInterval(), TimeUnit.SECONDS);

                return ops.exec();
            }
        });
        Long current = (Long) results.get(0);
        return new Rate(policy.getLimit(), Math.max(0, policy.getLimit() - current), time * (policy.getRefreshInterval() * 1000));
    }
}
