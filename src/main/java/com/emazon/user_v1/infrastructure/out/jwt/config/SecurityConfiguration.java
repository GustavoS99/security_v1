package com.emazon.user_v1.infrastructure.out.jwt.config;

import com.emazon.user_v1.infrastructure.out.jwt.config.filter.JwtTokenValidator;
import com.emazon.user_v1.infrastructure.out.jwt.entry.point.AuthenticationEntryPointImpl;
import com.emazon.user_v1.infrastructure.out.jwt.handler.AccessDeniedHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.emazon.user_v1.infrastructure.input.rest.util.PathDefinition.*;
import static com.emazon.user_v1.util.GlobalConstants.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenValidator jwtTokenValidator;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(
                            HttpMethod.POST, USER.concat(SIGNUP_WAREHOUSE_WORKER)
                    ).hasRole(ADMIN);

                    authorizeRequests.requestMatchers(HttpMethod.POST, USER.concat(LOGIN)).permitAll();

                    authorizeRequests.requestMatchers(SWAGGER_UI).permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.GET, OPEN_API).permitAll();

                    authorizeRequests.anyRequest().denyAll();
                })
                .addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(customizer -> {
                    customizer.accessDeniedHandler(accessDeniedHandler);
                    customizer.authenticationEntryPoint(authenticationEntryPoint);
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
