package com.ecom.microservices.api_gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GlobalLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre-filter
        log.info("Logging from Global Pre: {}", exchange.getRequest().getURI());
        // post-filter
        return chain.filter(exchange).then(Mono.fromRunnable(() ->
                log.info("Logging from Global Post: {}", exchange.getResponse().getStatusCode())
        ));
    }

    @Override
    public int getOrder() {
        return 5;
    }
}
