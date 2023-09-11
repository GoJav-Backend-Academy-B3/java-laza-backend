package com.phincon.laza.security;

import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.security.jwt.JwtAccessDeniedHandler;
import com.phincon.laza.security.jwt.JwtAuthenticationEntryPoint;
import com.phincon.laza.security.jwt.JwtAuthenticationFilter;
import com.phincon.laza.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.phincon.laza.security.oauth2.SysOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final SysOAuth2UserService sysOAuth2UserService;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteListedRoutes).permitAll()
                        .requestMatchers(GET, adminListedRoutes).hasAuthority(ERole.ADMIN.name())
                        .requestMatchers(POST, adminListedRoutes).hasAuthority(ERole.ADMIN.name())
                        .requestMatchers(PUT, adminListedRoutes).hasAuthority(ERole.ADMIN.name())
                        .requestMatchers(PATCH, adminListedRoutes).hasAuthority(ERole.ADMIN.name())
                        .requestMatchers(DELETE, adminListedRoutes).hasAuthority(ERole.ADMIN.name())
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/oauth2/callback/**"))
                        .userInfoEndpoint(user -> user
                                .userService(sysOAuth2UserService))
                        .defaultSuccessUrl("/auth/token", true))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .build();
    }

    public static final String[] whiteListedRoutes = new String[]{
            "/error",
            "/auth/**",
            "/oauth2/**",
            "/size/**",
            "/category/**",
            "/product/**",
            "/review/**",
            "/provinces",
            "/cities",
            "/costs",
            "/existsProvince/**",
            "/existsCity/**",
            "/brands/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final String[] adminListedRoutes = new String[]{
            "/management/**"
    };
}
