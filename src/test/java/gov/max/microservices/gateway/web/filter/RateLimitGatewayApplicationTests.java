package gov.max.microservices.gateway.web.filter;

import static org.junit.Assert.*;

import gov.max.microservices.gateway.web.filter.ratelimit.RateLimitFilter;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.RoutesEndpoint;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = RateLimitZuulApplication.class)
@IntegrationTest({
    "server.port: 0",
    "zuul.ratelimit.enabled: true"
})
@DirtiesContext
public class RateLimitGatewayApplicationTests {
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private DiscoveryClientRouteLocator routes;

    @Autowired
    private RoutesEndpoint endpoint;

    @Test
    public void getUnauthenticated() {
        routes.addRoute("/self/**", "http://localhost:" + this.port + "/local");
        this.endpoint.reset();
        ResponseEntity<String> response = new TestRestTemplate().getForEntity("http://localhost:" + port + "/self/1", String.class);
        assertNotNull(response.getHeaders().get(RateLimitFilter.Headers.LIMIT));
        assertNotNull(response.getHeaders().get(RateLimitFilter.Headers.REMAINING));
        assertNotNull(response.getHeaders().get(RateLimitFilter.Headers.RESET));
    }
}

@Configuration
@EnableAutoConfiguration(exclude = RedisAutoConfiguration.class)
@RestController
@EnableZuulProxy
class RateLimitZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateLimitZuulApplication.class);
    }

    @RequestMapping(value = "/local/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id) {
        return "Gotten " + id + "!";
    }
}