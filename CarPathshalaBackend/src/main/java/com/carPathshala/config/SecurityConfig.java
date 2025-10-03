package com.carPathshala.config;

import com.carPathshala.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Password encoder using BCrypt for secure hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager bean, used in login flows.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Main security configuration.
     * Configures:
     *  - Stateless session management
     *  - JWT-based authentication
     *  - Role-based access control
     *  - Public vs protected APIs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Disable CSRF for APIs
            .csrf(csrf -> csrf.disable())

            // ✅ No session, everything via JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth
                // 🔹 Authentication APIs → Public
                .requestMatchers("/api/auth/**").permitAll()

                // 🔹 Newsletter APIs → Public Subscribe, Admin View
                .requestMatchers(HttpMethod.POST, "/api/newsletter/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/newsletter/**").hasRole("ADMIN")

                // 🔹 Blog APIs
                .requestMatchers(HttpMethod.GET, "/api/blogs/**").permitAll()   // Anyone can view blogs
                .requestMatchers(HttpMethod.POST, "/api/blogs/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.PUT, "/api/blogs/**").hasAnyRole("ADMIN", "EDITOR")
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/**").hasRole("ADMIN")

                // 🔹 Swagger & Docs → Public (optional, for development)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // 🔹 Everything else → Must be authenticated
                .anyRequest().authenticated()
            )

            // ✅ Add JWT filter before Spring Security's UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}