package com.sapient.springsecurity.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		//Add users for in-memory authentication.
		UserBuilder users = User.withDefaultPasswordEncoder();
		auth.inMemoryAuthentication()
		.withUser(users.username("Susmit").password("Susmit7#").roles("EMPLOYEE", "MANAGER"))
		.withUser(users.username("Babloo").password("Babloo").roles("EMPLOYEE"))
		.withUser(users.username("Bibhu").password("Bibhu#").roles("EMPLOYEE", "ADMIN"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests() // Restrict access on based of HttpServletRequest.
		// Commenting this line as we want to restrict the access based on user roles.
		//.anyRequest().authenticated() // Any request coming to the application must be authenticated
		.antMatchers("/").hasRole("EMPLOYEE")
		.antMatchers("/leaders/**").hasRole("MANAGER")
		.antMatchers("/systems/**").hasRole("ADMIN")
		.and().formLogin(). // Customizing the form login
		loginPage("/myLoginPage"). // Our own jsp form implementation
		loginProcessingUrl("/authenticateTheUser").// Login form should POST data to this URL for processing (userId & Password)
		permitAll().// Permit all incoming requests for authentication (It fixed the issue Page load failed with error: too many HTTP redirects)
		and().
		exceptionHandling().accessDeniedPage("/access-denied"); // Custom handling of exception.
		/*logout(). // adding logout feature to our application.
		permitAll();*/ // Permit all incoming requests for authentication.
	}
}
