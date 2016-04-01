package gov.max.microservices.gateway.dashboard.repository.eureka;

import gov.max.microservices.gateway.dashboard.model.InstanceHistory;
import gov.max.microservices.gateway.dashboard.repository.RegistryRepository;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Pair;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import gov.max.microservices.gateway.dashboard.model.DashboardApplication;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Eureka registry implementation of application repository
 */
public class LocaleEurekaRepository extends EurekaRepository implements RegistryRepository {

    private final PeerAwareInstanceRegistry registry;

    public LocaleEurekaRepository(PeerAwareInstanceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Collection<DashboardApplication> findAll() {
        return registry.getSortedApplications().stream()
            .map(TO_APPLICATION)
            .collect(Collectors.toList());
    }

    @Override
    public DashboardApplication findByName(String name) {
        return TO_APPLICATION.apply(registry.getApplication(name));
    }

    @Override
    public List<InstanceHistory> getCanceledInstanceHistory() {
        return registry.getLastNCanceledInstances().stream().map(TO_REGISTRY_HISTORY).collect(Collectors.toList());
    }

    @Override
    public List<InstanceHistory> getRegisteredInstanceHistory() {
        return registry.getLastNRegisteredInstances().stream().map(TO_REGISTRY_HISTORY).collect(Collectors.toList());
    }
    protected InstanceInfo findInstanceInfo(String id) {
        String[] instanceIds = id.split("_", 2);
        return registry.getInstanceByAppAndId(instanceIds[0], instanceIds[1].replaceAll("_", "."));
    }

    private Function<Pair<Long, String>, InstanceHistory> TO_REGISTRY_HISTORY = history -> new InstanceHistory(history.second(), new Date(history.first()));
}
