package gov.max.microservices.gateway.dashboard;

import gov.max.microservices.gateway.dashboard.config.EnableDashboard;
import gov.max.microservices.gateway.dashboard.model.DashboardApplication;
import gov.max.microservices.gateway.dashboard.model.Instance;
import gov.max.microservices.gateway.dashboard.model.InstanceHistory;
import gov.max.microservices.gateway.dashboard.repository.ApplicationRepository;
import gov.max.microservices.gateway.dashboard.repository.RegistryRepository;
import gov.max.microservices.gateway.dashboard.turbine.MockStreamServlet;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration test to verify the correct functionality of the REST API.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DashboardApplicationTest.DashboardApplicationMock.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8080"})
public class DashboardApplicationTest {

    @Value("${server.port}")
    private int port = 8080;

    @Test
    public void testGetApplications() {
        @SuppressWarnings("rawtypes")
        ResponseEntity<List> entity = new TestRestTemplate().getForEntity("http://localhost:" + port + "/api/applications", List.class);
        assertNotNull(entity.getBody());
        assertEquals(2, entity.getBody().size());
        Map<String, Object> application = (Map<String, Object>) entity.getBody().get(0);
        assertEquals("MESSAGES", application.get("name"));
        assertEquals(3, ((List) application.get("instances")).size());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Configuration
    @EnableAutoConfiguration(exclude =
        LiquibaseAutoConfiguration.class
    )
    @EnableDashboard
    public static class DashboardApplicationMock {

        public static void main(String[] args) {
            SpringApplication.run(DashboardApplicationMock.class, args);
        }

        @Bean
        public ServletRegistrationBean hystrixStreamServlet() {
            return new ServletRegistrationBean(new MockStreamServlet("/hystrix.stream"), "/hystrix.stream");
        }

        @Bean
        public ApplicationRepository eurekaRepository() {
            return new ApplicationRepository() {

                @Override
                public Collection<DashboardApplication> findAll() {
                    return ImmutableList.of(
                        new DashboardApplication("MESSAGES",
                            ImmutableList.of(
                                new Instance("http://localhost:8761", "INSTANCE 1", "ID1", "UP"),
                                new Instance("http://localhost:8002", "INSTANCE 2", "ID2", "DOWN"),
                                new Instance("http://localhost:8003", "INSTANCE 3", "ID3", "STARTING")
                            )),
                        new DashboardApplication("FRONT",
                            ImmutableList.of(
                                new Instance("http://localhost:8001", "FRONT INSTANCE 1", "FRONT ID1", "OUT_OF_SERVICE"),
                                new Instance("http://localhost:8002", "FRONT INSTANCE 2", "FRONT ID2", "DOWN"),
                                new Instance("http://localhost:8003", "FRONT INSTANCE 3", "FRONT ID3", "UNKNOWN")
                            ))
                    );
                }

                @Override
                public DashboardApplication findByName(String name) {
                    return new DashboardApplication(name, null);
                }

                @Override
                public String getApplicationCircuitBreakerStreamUrl(String name) {
                    if (name.equalsIgnoreCase("MESSAGES")) {
                        return "http://localhost:8761/hystrix.stream";
                    } else {
                        return "http://localhost:8761/hystrixFail.stream";
                    }
                    //return "http://localhost:8001/turbine.stream?cluster="+name;
                }

                @Override
                public String getInstanceCircuitBreakerStreamUrl(String instanceId) {
                    if (instanceId.equalsIgnoreCase("ID1")) {
                        return "http://localhost:8761/hystrix.stream";
                    } else {
                        return "http://localhost:8761/hystrixFail.stream";
                    }
                }

                @Override
                public Instance findInstance(String id) {
                    return new Instance("http://localhost:8002", "INSTANCE " + id, id, "UP");
                }

                @Override
                public String getInstanceManagementUrl(String id) {
                    return "http://localhost:8761/";
                }
            };
        }

        @Bean
        public RegistryRepository eurekaRegistryRepository() {

            return new RegistryRepository() {

                @Override
                public List<InstanceHistory> getRegisteredInstanceHistory() {
                    return ImmutableList.of(
                        new InstanceHistory("INSTANCE 1", new Date()),
                        new InstanceHistory("INSTANCE 2", new Date()),
                        new InstanceHistory("INSTANCE 3", new Date())
                    );
                }

                @Override
                public List<InstanceHistory> getCanceledInstanceHistory() {
                    return ImmutableList.of(
                        new InstanceHistory("CANCELLED INSTANCE 1", new Date()),
                        new InstanceHistory("CANCELLED INSTANCE 2", new Date()),
                        new InstanceHistory("CANCELLED INSTANCE 3", new Date())
                    );
                }
            };
        }
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        return factory;
    }

}
