package gov.max.microservices.gateway.config;

import gov.max.microservices.gateway.zuul.accesscontrol.AccessControlFilter;
import gov.max.microservices.gateway.zuul.ratelimiting.InMemoryRateLimiter;
import gov.max.microservices.gateway.zuul.ratelimiting.RateLimitingFilter;
import gov.max.microservices.gateway.zuul.ratelimiting.RateLimitingProperties;
import gov.max.microservices.gateway.zuul.ratelimiting.RateLimiter;
import gov.max.microservices.gateway.zuul.ratelimiting.redis.RedisRateLimiter;
import gov.max.microservices.gateway.zuul.responserewriting.SwaggerBasePathRewritingFilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class GatewayConfiguration {

    @Configuration
    public static class SwaggerBasePathRewritingConfiguration {

        @Bean
        public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
            return new SwaggerBasePathRewritingFilter();
        }
    }

    @Configuration
    public static class AccessControlFilterConfiguration {

        @Bean
        public AccessControlFilter accessControlFilter() {
            return new AccessControlFilter();
        }
    }

    /**
     * Configures the Zuul filter that limits the number of API calls per user.
     * <p>
     * For this filter to work, you need to have:
     * <ul>
     * <li>A working Redis store
     * </ul>
     */
    @Configuration
    @EnableConfigurationProperties(RateLimitingProperties.class)
    @ConditionalOnProperty("zuul.ratelimit.enabled")
//    @ConditionalOnProperty("max.gateway.rate-limiting.enabled")
    public static class RateLimitingConfiguration {

        @Bean
        public RateLimitingFilter rateLimiterFilter(RateLimiter rateLimiter, RateLimitingProperties rateLimitingProperties) {
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
}
