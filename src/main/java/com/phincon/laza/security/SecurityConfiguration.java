package com.phincon.laza.security;

import com.phincon.laza.security.jwt.JwtAccessDeniedHandler;
import com.phincon.laza.security.jwt.JwtAuthenticationEntryPoint;
import com.phincon.laza.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteListedRoutes).permitAll()
                        .requestMatchers(GET, adminListedRoutes).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(POST, adminListedRoutes).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(PUT, adminListedRoutes).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(PATCH, adminListedRoutes).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(DELETE, adminListedRoutes).hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .build();
    }

    public static final String[] whiteListedRoutes = new String[]{
            "/auth/**",
            "/size/**",
            "/category/**",
            "/product/**",
            "/provinces",
            "/cities",
            "/costs"
    };

    private final String[] getAdminListedRoutes = new String[]{
            "/users",
            "/product",
    };

    private final String[] postAdminListedRoutes = new String[]{
            "/size/create",
            "/category/create",
    };

    private final String[] putAdminListedRoutes = new String[]{
    };

    private final String[] patchAdminListedRoutes = new String[]{
            "/users/update/role",
    };

    private final String[] adminListedRoutes = new String[]{
            "/management/**"
    };
}
