package ru.cbr.rrror.service.gateway.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/built/**", "/main.css", "/error", "/logoutSuccessfull").permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2Login(withDefaults())
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/logoutSuccessfull")
                ;
    }
}


