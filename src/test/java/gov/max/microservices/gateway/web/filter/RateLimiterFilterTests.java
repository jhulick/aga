package gov.max.microservices.gateway.web.filter;

import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;

import gov.max.microservices.gateway.web.filter.ratelimit.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.security.Principal;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Vinicius Carvalho
 */
public class RateLimiterFilterTests {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();
    private RateLimitProperties properties;
    @Before
    public void setup(){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.clear();
        ctx.setRequest(this.request);
        ctx.setResponse(response);
        properties = new RateLimitProperties();
        properties.init();
    }

    @Test
    public void anonymousHeadersOk() throws Exception {
        RateLimiter limiter = new InMemoryRateLimiter();
        RateLimitFilter filter = new RateLimitFilter(limiter,properties);
        filter.run();
        RequestContext ctx = RequestContext.getCurrentContext();

        Policy policy = properties.getPolicies().get(Policy.PolicyType.ANONYMOUS);
        assertNull(ctx.get("error.status_code"));
        verifyHeaders(ctx);
        assertEquals(policy.getLimit().toString(), ctx.getResponse().getHeader(RateLimitFilter.Headers.LIMIT));
        assertEquals(policy.getLimit()-1, Long.parseLong(ctx.getResponse().getHeader(RateLimitFilter.Headers.REMAINING)));
        assertTrue(Long.valueOf(ctx.getResponse().getHeader(RateLimitFilter.Headers.RESET)) > System.currentTimeMillis());
    }

    @Test
    public void anonymousTooManyRequests() throws Exception{
        RateLimiter limiter = mock(RateLimiter.class);

        Rate sample = new Rate(2L,0L,10L);

        when(limiter.consume(any(Policy.class),anyString())).thenReturn(sample);
        RateLimitFilter filter = new RateLimitFilter(limiter,properties);
        try{
            filter.run();
        }catch (Exception e){}
        RequestContext ctx = RequestContext.getCurrentContext();

        assertEquals(sample.getLimit().toString(), ctx.getResponse().getHeader(RateLimitFilter.Headers.LIMIT));
        assertEquals(sample.getRemaining().toString(), ctx.getResponse().getHeader(RateLimitFilter.Headers.REMAINING));
        assertEquals(sample.getReset().toString(), ctx.getResponse().getHeader(RateLimitFilter.Headers.RESET));
        assertEquals(429,ctx.getResponseStatusCode());
    }

    @Test
    public void authenticatedHeadersOk() throws Exception{
        Policy anonymoousPolicy = properties.getPolicies().get(Policy.PolicyType.ANONYMOUS);
        Policy authPolicy = new Policy(60L,600L);
        properties.getPolicies().put(Policy.PolicyType.AUTHENTICATED,authPolicy);

        RateLimiter limiter = new InMemoryRateLimiter();
        RateLimitFilter filter = new RateLimitFilter(limiter,properties);

        filter.run();
        RequestContext ctx = RequestContext.getCurrentContext();
        verifyHeaders(ctx);
        String anonymousRemaining = ctx.getResponse().getHeader(RateLimitFilter.Headers.REMAINING);
        this.request.setUserPrincipal(new Principal() {
            @Override
            public String getName() {
                return "lucy";
            }
        });
        filter.run();
        String authRemaining = ctx.getResponse().getHeader(RateLimitFilter.Headers.REMAINING);
        assertTrue(Long.parseLong(authRemaining) > Long.parseLong(anonymousRemaining));

    }

    private Object getHeader(List<Pair<String, String>> headers, String key) {
        String value = null;
        for (Pair<String, String> pair : headers) {
            if (pair.first().toLowerCase().equals(key.toLowerCase())) {
                value = pair.second();
                break;
            }
        }
        return value;
    }

    private void verifyHeaders(RequestContext ctx){
        assertNotNull(ctx.getResponse().getHeader(RateLimitFilter.Headers.LIMIT));
        assertNotNull(ctx.getResponse().getHeader(RateLimitFilter.Headers.REMAINING));
        assertNotNull(ctx.getResponse().getHeader(RateLimitFilter.Headers.RESET));
    }

}
