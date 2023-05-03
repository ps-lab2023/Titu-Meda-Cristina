package com.nagarro.af.bookingtablesystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**")
                .permitAll()
                .requestMatchers("/admins/**", "/customers/**", "/managers/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/restaurants").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/restaurants/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/restaurants/{restaurant_id}/manager/{manager_id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/restaurants/{restaurant_id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_RESTAURANT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/bookings").hasAuthority("ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/bookings/{id}").hasAnyAuthority("ROLE_RESTAURANT_MANAGER", "ROLE_CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
