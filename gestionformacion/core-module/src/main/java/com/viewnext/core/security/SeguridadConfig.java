package com.viewnext.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SeguridadConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/autentificacion/**").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(filtroAutentificacionJWT(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(filtroAutentificacionOTP(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
	}
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public FiltroAutentificacionJWT filtroAutentificacionJWT() {
        return new FiltroAutentificacionJWT();
    }
    
    @Bean
    public FiltroAutentificacionOTP filtroAutentificacionOTP() {
        return new FiltroAutentificacionOTP();
    }
    
    @Bean
    public CredentialRepositoryImpl credentialRepository() {
        return new CredentialRepositoryImpl();
    }

}
    