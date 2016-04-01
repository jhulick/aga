package gov.max.microservices.gateway.dashboard.repository;

import gov.max.microservices.gateway.dashboard.model.DashboardApplication;
import gov.max.microservices.gateway.dashboard.model.Instance;

import java.util.Collection;

/**
 * DashboardApplication repository interface
 */
public interface ApplicationRepository {

    /**
     * @return all Applications registered;
     */
    Collection<DashboardApplication> findAll();

    /**
     * @param name the applications name
     * @return all Applications with the specified name;
     */
    DashboardApplication findByName(String name);

    /**
     * Return circuit breaker url to application
     * @param name Name of application
     * @return Circuit Breaker Stream
     */
    String getApplicationCircuitBreakerStreamUrl(String name);

    /**
     * Return circuit breaker url to instance
     * @param instanceId Id of instance
     * @return Circuit Breaker Stream
     */
    String getInstanceCircuitBreakerStreamUrl(String instanceId);

    /**
     * @param id the instance by id
     * @return the Instance with the specified id;
     */
    Instance findInstance(String id);

    /**
     * Return management url to service
     * @param id Id of instance
     * @return Management URL
     */
    String getInstanceManagementUrl(String id);
}
