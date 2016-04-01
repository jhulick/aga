package gov.max.microservices.gateway.dashboard.config;

import com.netflix.discovery.EurekaClient;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import gov.max.microservices.gateway.dashboard.repository.ApplicationRepository;
import gov.max.microservices.gateway.dashboard.repository.eureka.LocaleEurekaRepository;
import gov.max.microservices.gateway.dashboard.repository.eureka.RemoteEurekaRepository;
import gov.max.microservices.gateway.dashboard.stream.CircuitBreakerStreamServlet;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Cloud Dashboard WebApp configuration
 */
@Configuration
@ComponentScan("gov.max.microservices.gateway.dashboard")
public class CloudDashboardConfig extends WebMvcConfigurerAdapter {

    @ConditionalOnClass(name = "com.netflix.eureka.registry.PeerAwareInstanceRegistry")
    @ConditionalOnMissingBean(ApplicationRepository.class)
    public static class LocalEureka {

        @Bean
        public ApplicationRepository eurekaRepository(PeerAwareInstanceRegistry peerAwareInstanceRegistry) {
            return new LocaleEurekaRepository(peerAwareInstanceRegistry);
        }
    }

    @ConditionalOnMissingClass(name = "com.netflix.eureka.registry.PeerAwareInstanceRegistry")
    @ConditionalOnClass(name = "com.netflix.discovery.EurekaClient")
    @ConditionalOnMissingBean(ApplicationRepository.class)
    public static class RemoteEureka {

        @Bean
        public ApplicationRepository remoteEurekaRepository(EurekaClient eurekaClient) {
            return new RemoteEurekaRepository(eurekaClient);
        }
    }

    @Bean
    @Autowired
    public ServletRegistrationBean circuitBreakerStreamServlet(ApplicationRepository repository) {
        return new ServletRegistrationBean(new CircuitBreakerStreamServlet(HttpClient(), repository), "/circuitBreaker.stream");
    }

    @Bean
    public HttpClient HttpClient() {
        return HttpClients.custom()
            .setMaxConnTotal(100)
            .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(2000).build())
            .setDefaultRequestConfig(RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(1000).build())
            .build();
    }
}
