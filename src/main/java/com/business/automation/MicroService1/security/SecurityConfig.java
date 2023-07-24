package com.business.automation.MicroService1.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig  {

    @Value("${spring.application.name}")
    String resourceName;


    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

        http
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().authenticated()
                )
                .addFilterBefore(new UserIdentityContextFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .oauth2ResourceServer(oauth2 -> oauth2
                                .jwt(jwt -> jwt
                                        .jwtAuthenticationConverter(new CustomAuthenticationConverter(resourceName))
                                )
                );

        return http.build();

    }


}
