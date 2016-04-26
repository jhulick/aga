package gov.max.microservices.gateway.zuul.ratelimiting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import gov.max.microservices.gateway.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.Date;

/**
 * Zuul filter for limiting the number of HTTP calls per client.
 */
public class RateLimitingFilter extends ZuulFilter {

    private final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    private static final String TIME_PERIOD = "hour";

    private long rateLimit = 100000L;

    private final RateLimiter limiter;

    private RateLimitProperties properties;

    public RateLimitingFilter(RateLimiter limiter, RateLimitProperties properties) {
        this.limiter = limiter;
        this.properties = properties;
//        this.rateLimit = maxProperties.getGateway().getRateLimiting().getLimit();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        // specific APIs can be filtered out using
        // if (RequestContext.getCurrentContext().getRequest().getRequestURI().startsWith("/api")) { ... }
        return true;
    }

    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();
        Policy policy = findRequestPolicy(request);
        String key = findKey(request);
        Rate rate = limiter.consume(policy, key);
        response.setHeader(Headers.LIMIT, rate.getLimit().toString());
        response.setHeader(Headers.REMAINING, String.valueOf(Math.max(rate.getRemaining(), 0)));
        response.setHeader(Headers.RESET, rate.getReset().toString());
        if (rate.getRemaining() <= 0) {
            ctx.setResponseStatusCode(429);
            ctx.put("rateLimitExceeded","true");
            throw new RuntimeException("");
        }
        return null;
    }

    private void apiLimitExceeded() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody("API rate limit exceeded");
            ctx.setSendZuulResponse(false);
        }
    }

    private Policy findRequestPolicy(HttpServletRequest request) {
        Policy policy = (request.getUserPrincipal() == null) ? properties.getPolicies().get(Policy.PolicyType.ANONYMOUS) : properties.getPolicies().get(Policy.PolicyType.AUTHENTICATED);
        return policy;
    }

    /**
     * The ID that will identify the limit: the user login or the user IP address.
     */
    private String findKey(HttpServletRequest request) {
        String key = (request.getUserPrincipal() == null) ? request.getRemoteAddr() : request.getUserPrincipal().getName();
        return key;
    }

    /**
     * The ID that will identify the limit: the user login or the user IP address.
     */
    private String getId(HttpServletRequest httpServletRequest) {
        String login = SecurityUtils.getCurrentUserLogin();
        if (login != null) {
            return login;
        } else {
            return httpServletRequest.getRemoteAddr();
        }
    }

    /**
     * The period for which the rate is calculated.
     */
    private Date getPeriod() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MINUTE);
        return calendar.getTime();
    }

    public static interface Headers {
        String LIMIT = "X-RateLimit-Limit";
        String REMAINING = "X-RateLimit-Remaining";
        String RESET = "X-RateLimit-Reset";
    }
}
