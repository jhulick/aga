package gov.max.microservices.gateway.dashboard.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable Spring Cloud Dashboard
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CloudDashboardConfig.class)
public @interface EnableCloudDashboard {

}
