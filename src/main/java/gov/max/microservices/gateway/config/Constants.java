package gov.max.microservices.gateway.config;

/**
 * DashboardApplication constants.
 */
public final class Constants {

    // Spring profile for development, production and "test"
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_TEST = "test";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    public static final String SPRING_PROFILE_NO_SWAGGER = "no-swagger";

    public static final String SYSTEM_ACCOUNT = "system";

    private Constants() {
    }
}
