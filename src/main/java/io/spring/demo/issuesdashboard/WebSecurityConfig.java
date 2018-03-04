package io.spring.demo.issuesdashboard;

//import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest;
//import org.springframework.boot.autoconfigure.security.StaticResourceRequest;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(
				User.withUsername("user").password("password")
						.passwordEncoder(passwordEncoder()::encode)
						.authorities("ROLE_USER").build(),
				User.withUsername("admin").password("admin")
						.passwordEncoder(passwordEncoder()::encode)
						.authorities("ROLE_ACTUATOR", "ROLE_ADMIN", "ROLE_USER").build());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin").hasRole("ADMIN")
				.requestMatchers(EndpointRequest.to("info", "health")).permitAll()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.antMatchers("/events/**").hasRole("USER")
				.antMatchers("/**").permitAll()
				.and().httpBasic();
	}
}
