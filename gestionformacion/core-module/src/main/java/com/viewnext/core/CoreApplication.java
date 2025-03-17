package com.viewnext.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.viewnext.core.repositories") 
public class CoreApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
