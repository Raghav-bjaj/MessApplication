package org.messplacement.messsecond.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Add the CORS configuration to the security filter chain.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Your existing CSRF and authorization rules remain.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        // Note: formLogin and httpBasic are often not needed for stateless APIs
        // that use tokens, but we can leave them for now.

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 3. Define the allowed origins (your Angular app's URLs).
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://mess-application-front-end-angular.vercel.app"
        ));

        // 4. Define the allowed HTTP methods.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 5. Define the allowed headers.
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // 6. Allow credentials if you use them (e.g., for cookies).
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths in your application.
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
