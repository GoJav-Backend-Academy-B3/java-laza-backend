package com.phincon.laza.security;

import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.security.jwt.JwtAccessDeniedHandler;
import com.phincon.laza.security.jwt.JwtAuthenticationEntryPoint;
import com.phincon.laza.security.jwt.JwtAuthenticationFilter;
import com.phincon.laza.security.oauth2.OAuth2FailureHandler;
import com.phincon.laza.security.oauth2.OAuth2SuccessHandler;
import com.phincon.laza.security.oauth2.SysOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    private final SysOAuth2UserService sysOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
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
                                .baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/oauth2/callback/**"))
                        .userInfoEndpoint(user -> user
                                .userService(sysOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler))
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
            "/error",
            "/auth/**",
            "/oauth2/**",
            "/rmq/**",
            "/size/**",
            "/category/**",
            "/products/**",
            "/provinces",
            "/cities",
            "/costs",
            "/callback/**",
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
            "/swagger-ui.html",
            "/midtrans/gopay-callback"
    };

    private final String[] adminListedRoutes = new String[]{
            "/management/**"
    };
}
