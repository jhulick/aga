package gov.max.microservices.gateway.dashboard.repository;

import gov.max.microservices.gateway.dashboard.model.InstanceHistory;

import java.util.List;

/**
 * DashboardApplication repository interface
 */
public interface RegistryRepository {

    /**
     * Return list of last registered instance in registry
     * @return List of 1000 last instance registered
     */
    List<InstanceHistory> getRegisteredInstanceHistory();

    /**
     * Return list of last canceled instance in registry
     * @return List of 1000 last instance cancelled
     */
    List<InstanceHistory> getCanceledInstanceHistory();
}
