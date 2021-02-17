package root.security_config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import root.api.sign_in.CommunicatorAuthenticationFailureHandler;
import root.api.sign_in.CommunicatorAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService communicatorDetailsService;
	
	@Bean
	public PasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(communicatorDetailsService);
		provider.setPasswordEncoder(getBCryptPasswordEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationSuccessHandler getCommunicatorAuthenticationSuccessHandler() {
		return new CommunicatorAuthenticationSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler getCommunicatorAuthenticationFailureHandler() {
		return new CommunicatorAuthenticationFailureHandler();
	}
	
	@Bean
	public PersistentTokenRepository getPersistentTokenRepository() {
		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
		repository.setDataSource(dataSource);
		return repository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(getAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.authorizeRequests()
				.antMatchers("/", "/sign-in-page", "sign-up-page", "sign-in").permitAll()
				.antMatchers("/**").hasRole("USER")
				.and()
			.formLogin()
				.loginPage("/sign-in-page")
				.loginProcessingUrl("/sign-in")
				.successHandler(getCommunicatorAuthenticationSuccessHandler())
				.failureHandler(getCommunicatorAuthenticationFailureHandler())
				.and()
			.rememberMe().tokenRepository(getPersistentTokenRepository()).userDetailsService(communicatorDetailsService);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**","/webjars/**");
	}
	
	

}

























