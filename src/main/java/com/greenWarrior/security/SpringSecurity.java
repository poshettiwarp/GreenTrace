package com.greenWarrior.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	private UserDetailsService CustomUserDetailsServiceImpl;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * Custom Users with hardCoded values, instead of only one user in properties
	 * file we can have multiple user inMomory,without database but remember if you
	 * are using both InMemory and DB user then either you have to implement
	 * DAOAuthenticationProvider method for UserDetailsService and DBUser or Simply
	 * you have to comment the CustomUserDetailsServiceImpl(don't mark as bean) so
	 * it will not interrupt with inMemoryUserDetailsService else it will always
	 * choose database users and give 401 HTTP response as InMemeory users are not
	 * present in database.
	 */

	/*
	 * @Bean UserDetailsService userDetailsService() {
	 * 
	 * UserDetails user1 =
	 * User.builder().username("sita_ram").password(passwordEncoder().encode(
	 * "sitaRam@123")) .roles("USER").build();
	 * 
	 * UserDetails user2 = User.builder().username("radhee_shyam")
	 * .password(passwordEncoder().encode("radheeShyam@123")).roles("ADMIN").build()
	 * ;
	 * 
	 * return new InMemoryUserDetailsManager(user1, user2);
	 * 
	 * }
	 */

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests(auth -> auth.antMatchers("/api/auth/**", "/public/**").permitAll()
						.antMatchers("/api/tree-search/by-planter", "/api/user/", "/api/user-search/by-user-name",
								"/api/user-search/by-role")
						.hasRole("ADMIN").antMatchers(HttpMethod.POST, "/api/trees/**").hasRole("USER")
						.antMatchers(HttpMethod.PATCH, "/api/trees/**").hasRole("USER")
						.antMatchers(HttpMethod.DELETE, "/api/trees/**").hasRole("USER")
						.antMatchers(HttpMethod.DELETE, "/api/user/{id}").hasAnyRole("ADMIN", "USER")
						.antMatchers(HttpMethod.GET, "/api/trees/**", "/api/tree-search/by-local-name",
								"api/tree-search/by-species-name", "api/tree-search/by-date",
								"api/tree-search/by-date-range", "api/tree-search/by-stage", "/api/user/{id}",
								"/api/user/{id}/personal-details", "api/user/{id}/address", "/api/user/{id}/status",
								"/api/user/stats/{id}", "/api/user//{userName}/update-password")
						.hasAnyRole("ADMIN", "USER")

						.anyRequest().authenticated())
				.httpBasic().and().formLogin();
		return http.build();

	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(CustomUserDetailsServiceImpl)
				.passwordEncoder(passwordEncoder()).and().build();
	}

}
