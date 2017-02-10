package com.samsanort.restingbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * --------------------------------------
 * Resting Bank - main application class.
 * --------------------------------------
 *
 * by Samuel Sanchez.
 */

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
