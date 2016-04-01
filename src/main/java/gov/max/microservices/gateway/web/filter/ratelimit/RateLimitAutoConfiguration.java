package gov.max.microservices.gateway.web.filter.ratelimit;

import gov.max.microservices.gateway.web.filter.ratelimit.redis.RedisRateLimiter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty("zuul.ratelimit.enabled")

public class RateLimitAutoConfiguration {

    @Bean
    public RateLimitFilter rateLimiterFilter(RateLimiter rateLimiter, RateLimitProperties rateLimitProperties){
        return new RateLimitFilter(rateLimiter,rateLimitProperties);
    }

    @ConditionalOnMissingBean(name = {"redisTemplate"})
    static class InMemoryRateLimitConfiguration {

        @Bean
        public RateLimiter inMemoryRateLimiter() {
            InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter();
            return rateLimiter;
        }

    }


    @ConditionalOnBean(name = {"redisTemplate"})
    @ConditionalOnClass(RedisTemplate.class)
    static class RedisRateLimiterConfiguration {

        @Bean
        public RateLimiter redisRateLimiter(RedisTemplate redisTemplate) {
            RedisRateLimiter rateLimiter = new RedisRateLimiter(redisTemplate);
            return rateLimiter;
        }
    }

}