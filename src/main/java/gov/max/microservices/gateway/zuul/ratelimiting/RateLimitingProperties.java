package gov.max.microservices.gateway.zuul.ratelimiting;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("zuul.ratelimit")
public class RateLimitingProperties {

    private Map<Policy.PolicyType, Policy> policies = new LinkedHashMap<>();

    private boolean enabled;

    @PostConstruct
    public void init() {
        if (policies.get(Policy.PolicyType.ANONYMOUS) == null) {
            policies.put(Policy.PolicyType.ANONYMOUS, new Policy(60L, 60L));
        }
    }

    public Map<Policy.PolicyType, Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Map<Policy.PolicyType, Policy> policies) {
        this.policies = policies;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
