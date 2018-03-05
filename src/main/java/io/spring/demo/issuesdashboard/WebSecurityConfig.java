package io.spring.demo.issuesdashboard;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .matchers(EndpointRequest.to("info", "health")).permitAll()
                .matchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                .pathMatchers("/events/**").hasRole("USER")
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/**", "/login").permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic().and()
                .formLogin()
                .loginPage("/login");
        return http.build();
    }


    //TODO
    @Bean
    PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder passwordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        passwordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return passwordEncoder;
    }
}
