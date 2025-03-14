package com.bank.api_gateway_service.Util;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;

    public JwtAuthenticationGatewayFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        return webClientBuilder.build()
                .post()
                .uri("http://USER-SERVICE/auth/validate") // Eureka-based service name
                .bodyValue(token)
                .retrieve()
                .bodyToMono(UserDetailsDto.class)
                .flatMap(userDetails -> {
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", userDetails.getUsername())
                            .header("X-User-Roles", String.join(",", userDetails.getRoles())) // Pass roles as a comma-separated string
                            .build();
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                });

    }
}
