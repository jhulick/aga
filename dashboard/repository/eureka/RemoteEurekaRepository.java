package gov.max.microservices.gateway.dashboard.repository.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import gov.max.microservices.gateway.dashboard.model.Application;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Eureka registry implementation of application repository
 */
public class RemoteEurekaRepository extends EurekaRepository {

    private final EurekaClient eurekaClient;

    public RemoteEurekaRepository(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public Collection<Application> findAll() {
        return eurekaClient.getApplications().getRegisteredApplications().stream()
            .map(TO_APPLICATION)
            .collect(Collectors.toList());
    }

    @Override
    public Application findByName(String name) {
        return TO_APPLICATION.apply(eurekaClient.getApplications().getRegisteredApplications(name));
    }

    protected InstanceInfo findInstanceInfo(String id) {
        String[] instanceIds = id.split("_", 2);
        return eurekaClient.getApplication(instanceIds[0]).getByInstanceId(instanceIds[1].replaceAll("_", "."));
    }
}
