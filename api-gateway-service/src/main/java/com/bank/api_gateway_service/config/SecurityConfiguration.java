package com.bank.api_gateway_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for APIs
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/",
                                "/swagger-ui/**",  //swagger
                                "/swagger-ui.html", // Older Swagger UI URL
                                "/v3/api-docs/**",
                                "/api/users/**").permitAll() // Public endpoints (e.g., login, register)
                        .anyExchange().authenticated() // Secure all other routes
                )
                .build();
    }
}

