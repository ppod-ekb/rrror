package ru.cbr.rrror.service.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cbr.rrror.service.gateway.event.DbServiceZuulFilterAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Configuration
public class ZuulFilterConfig {

    private final DbServiceZuulFilterAction event;

    @Bean
    ZuulFilter getZuulFilter() {
        return new ZuulFilter() {
            private final Supplier<RequestContext> ctx = () -> RequestContext.getCurrentContext();
            private final Predicate<RequestContext> uriFilterCondition = (ctx) -> ctx.getRequest().getRequestURI().startsWith("/db-service/api");
            private final Supplier<List<Runnable>> actions = () -> event.getActions();

            @Override
            public String filterType() {
                return FilterConstants.POST_TYPE;
            }

            @Override
            public int filterOrder() {
                return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
            }

            private boolean isDbServiceRequest() {
                return uriFilterCondition.test(ctx.get());
            }

            @Override
            public boolean shouldFilter() {
                return isDbServiceRequest();
            }

            @Override
            public Object run() throws ZuulException {
                RequestContext context = ctx.get();
                HttpServletRequest request = context.getRequest();
                HttpServletResponse response = context.getResponse();



                log.info(String.format(">>> zuul filter: %s request to %s with status %s", request.getMethod(), request.getRequestURL().toString(), response.getStatus()));
                for (Runnable action : actions.get()) {
                    action.run();
                }
                return null;
            }
        };
    }

    @Slf4j
    @AllArgsConstructor
    static class DbServiceZuulFilter extends ZuulFilter {

        private final Supplier<RequestContext> ctx = () -> RequestContext.getCurrentContext();
        private final Predicate<RequestContext> uriFilterCondition = (ctx) -> ctx.getRequest().getRequestURI().startsWith("/db-service/api");
        private final List<Runnable> actions;

        @Override
        public String filterType() {
            return FilterConstants.POST_TYPE;
        }

        @Override
        public int filterOrder() {
            return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
        }

        private boolean isDbServiceRequest() {
            return uriFilterCondition.test(ctx.get());
        }

        @Override
        public boolean shouldFilter() {
            return isDbServiceRequest();
        }

        @Override
        public Object run() throws ZuulException {
            RequestContext context = ctx.get();
            HttpServletRequest request = context.getRequest();
            HttpServletResponse response = context.getResponse();

            log.info(String.format(">>> zuul filter: %s request to %s", request.getMethod(), request.getRequestURL().toString()));
            for (Runnable action : actions) {
                action.run();
            }
            return null;
        }
    }
}
