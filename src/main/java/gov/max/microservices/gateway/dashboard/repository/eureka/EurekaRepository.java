package gov.max.microservices.gateway.dashboard.repository.eureka;

import gov.max.microservices.gateway.dashboard.model.DashboardApplication;
import gov.max.microservices.gateway.dashboard.model.Instance;
import gov.max.microservices.gateway.dashboard.repository.ApplicationRepository;

import com.netflix.appinfo.InstanceInfo;

import org.springframework.beans.factory.annotation.Value;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Eureka registry implementation of application repository
 */
public abstract class EurekaRepository implements ApplicationRepository {

    @Value("${spring.cloud.dashboard.turbine.url:http://localhost:${server.port}/turbine.stream}")
    private String turbineUrl;

    public abstract DashboardApplication findByName(String name);

    @Override
    public String getApplicationCircuitBreakerStreamUrl(String name) {
        if (findByName(name) == null) {
            return null;
        }
        return turbineUrl + "?cluster=" + name;
    }

    @Override
    public String getInstanceCircuitBreakerStreamUrl(String instanceId) {
        String url = getInstanceManagementUrl(instanceId);
        if (url == null) {
            return null;
        }
        return url + "/hystrix.stream";
    }

    @Override
    public Instance findInstance(String id) {
        return TO_INSTANCE.apply(findInstanceInfo(id));
    }

    @Override
    public String getInstanceManagementUrl(String id) {

        InstanceInfo info = findInstanceInfo(id);
        if (info == null) {
            return null;
        }

        String url = info.getHomePageUrl();
        if (info.getMetadata().containsKey("managementPath")) {
            url += info.getMetadata().get("managementPath");
        }

        return url;
    }

    protected abstract InstanceInfo findInstanceInfo(String id);

    protected Function<com.netflix.discovery.shared.Application, DashboardApplication> TO_APPLICATION = new Function<com.netflix.discovery.shared.Application, DashboardApplication>() {
        @Override
        public DashboardApplication apply(com.netflix.discovery.shared.Application app) {
            if (app == null) {
                return null;
            }
            return new DashboardApplication(app.getName(), app.getInstances().stream().map(TO_INSTANCE).sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList()));
        }
    };

    protected Function<InstanceInfo, Instance> TO_INSTANCE = instance -> {
        if (instance == null) {
            return null;
        }
        return new Instance(instance.getHomePageUrl(), instance.getId(), instance.getAppName() + "_" + instance.getId().replaceAll("\\.", "_"), instance.getStatus().toString());
    };
}
