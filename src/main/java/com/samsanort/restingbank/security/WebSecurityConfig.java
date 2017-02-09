package com.samsanort.restingbank.security;

import com.samsanort.restingbank.security.impl.AccessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * TODO describe
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic() // basic authentication (user/pwd provided on each request)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll() // allow user registration
                .anyRequest().fullyAuthenticated() // secure all other requests
        .and().csrf().disable(); // token validation disabled for the sake of simplicity
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AccessService accessService() {
        return new AccessServiceImpl();
    }
}