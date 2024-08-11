package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .authorizeExchange(exchange ->
                        exchange.pathMatchers(HttpMethod.GET, "/teams")
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors
                        .configurationSource(request -> {
                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(java.util.Collections.singletonList("http://localhost:3000"));
                            corsConfiguration.setAllowedMethods(java.util.Arrays.asList("*"));
                            corsConfiguration.setAllowedHeaders(java.util.Arrays.asList("*"));
                            corsConfiguration.setAllowCredentials(true);
                            return corsConfiguration;
                        })
                );
        return serverHttpSecurity.build();
    }
}
