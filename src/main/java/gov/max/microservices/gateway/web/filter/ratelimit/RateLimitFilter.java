package gov.max.microservices.gateway.web.filter.ratelimit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class RateLimitFilter extends ZuulFilter {

    private final RateLimiter limiter;

    private RateLimitProperties properties;

    public RateLimitFilter(RateLimiter limiter, RateLimitProperties properties) {
        this.limiter = limiter;
        this.properties = properties;
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

    private Policy findRequestPolicy(HttpServletRequest request) {
        Policy policy = (request.getUserPrincipal() == null) ? properties.getPolicies().get(Policy.PolicyType.ANONYMOUS) : properties.getPolicies().get(Policy.PolicyType.AUTHENTICATED);
        return policy;
    }

    private String findKey(HttpServletRequest request) {
        String key = (request.getUserPrincipal() == null) ? request.getRemoteAddr() : request.getUserPrincipal().getName();
        return key;
    }

    public static interface Headers {
        String LIMIT = "X-RateLimit-Limit";

        String REMAINING = "X-RateLimit-Remaining";

        String RESET = "X-RateLimit-Reset";
    }
}
