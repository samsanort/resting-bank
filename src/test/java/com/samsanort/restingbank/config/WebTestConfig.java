package com.samsanort.restingbank.config;

import static org.mockito.Mockito.mock;

import com.samsanort.restingbank.dataservice.AccountDataService;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.repository.UserRepository;
import com.samsanort.restingbank.security.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class WebTestConfig {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public UserDataService userDataService() { return mock(UserDataService.class); }

    @Bean
    public AccountDataService accountDataService() {
        return mock(AccountDataService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
}
