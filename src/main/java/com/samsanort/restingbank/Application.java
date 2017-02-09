package com.samsanort.restingbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * TODO add description
 */

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
