
package org.springframework.samples.petclinic.configuration;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	@Override


	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/users/new").permitAll()
				.antMatchers("/trainers").permitAll()
				.antMatchers("/trainers/{trainerId}").permitAll()
				.antMatchers("/organizations", "/organizations/{id}").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/owners/{ownerId}").hasAnyAuthority("owner","admin","veterinarian","trainer")			
				.antMatchers("/owners/{ownerId}/pets/{petId}/rehab/new").hasAnyAuthority("trainer")
				.antMatchers("/homeless-pets/**").hasAnyAuthority("veterinarian", "trainer")
				.antMatchers("/vets/**").authenticated()
				.antMatchers("/medicine/**").hasAnyAuthority("veterinarian", "admin")
				.antMatchers("/medical-record/**").authenticated()
				.antMatchers("/vets.xml").permitAll()
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/"); 
                // Configuración para que funcione la consola de administración 
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();

	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource).usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
			.authoritiesByUsernameQuery("select username, authority " + "from authorities " + "where username = ?").passwordEncoder(this.passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		return encoder;
	}

	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		GrantedAuthority authority = new SimpleGrantedAuthority("trainer");
		UserDetails userDetailsTrainer = new User("trainer1", "trainer1", Arrays.asList(authority));

		return new InMemoryUserDetailsManager(Arrays.asList(userDetailsTrainer));
	}

}
