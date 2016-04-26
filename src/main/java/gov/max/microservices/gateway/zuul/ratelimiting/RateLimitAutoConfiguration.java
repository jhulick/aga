package gov.max.microservices.gateway.zuul.ratelimiting;

import gov.max.microservices.gateway.zuul.ratelimiting.redis.RedisRateLimiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RateLimitingProperties.class)
@ConditionalOnProperty("zuul.ratelimit.enabled")
public class RateLimitAutoConfiguration {

    @Bean
    public RateLimitingFilter rateLimiterFilter(RateLimiter rateLimiter, RateLimitingProperties rateLimitingProperties){
        return new RateLimitingFilter(rateLimiter, rateLimitingProperties);
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
            return new RedisRateLimiter(redisTemplate);
        }
    }

}
