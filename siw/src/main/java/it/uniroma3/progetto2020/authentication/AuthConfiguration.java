package it.uniroma3.progetto2020.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.WebSecurityEnablerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;

import static it.uniroma3.progetto2020.model.Credentials.ADMIN_ROLE;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	DataSource datasource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
        http
                // authorization paragraph: here we define WHO can access WHICH pages
                .authorizeRequests()
                // anyone (authenticated or not) can access the welcome page, the login page, and the registration page
                .antMatchers(HttpMethod.GET, "/", "/index", "/login", "/utenti/register", "/progetto").permitAll()
                // anyone (authenticated or not) can send POST requests to the login endpoint and the register endpoint
                .antMatchers(HttpMethod.POST, "/login", "/utenti/register", "/progetto").permitAll()
                // only authenticated users with ADMIN authority can access the admin pag
                .antMatchers(HttpMethod.GET, "/admin").hasAnyAuthority(ADMIN_ROLE)
                // all authenticated users can access all the remaining other pages
                .anyRequest().authenticated()

                // login paragraph: here we define how to login
                // use formlogin protocol to perform login
                .and().formLogin()
                // after login is successful, redirect to the logged user homepage
                .defaultSuccessUrl("/home")

                // NOTE: using the default configuration, the /login endpoint is mapped to an auto-generated login page.
                // If we wanted to create a login page of own page, we would need to
                // - use the .loginPage() method in this configuration
                // - write a controller method that returns our login view when a GET method is sent to /login
                //   (but Spring would still handle the POST automatically)

                // logout paragraph: we are going to define here how to logout
                .and().logout()
                .logoutUrl("/logout")               // logout is performed when sending a GET to "/logout"
                .logoutSuccessUrl("/index");        // after logout is successful, redirect to /index page
    }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
        		.dataSource(this.datasource)
                //retrieve username and role
                .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
                //retrieve username, password and a boolean flag specifying whether the user is enabled or not (always enabled in our case)
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
