package com.viewnext.login;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.viewnext.core.presentation.config.LogAspect;

@SpringBootApplication
@EntityScan(basePackages = {"com.viewnext.core.business.model"})
@ComponentScan(basePackages = {"com.viewnext.core.presentation.config", "com.viewnext.login",
"com.viewnext.core.security"})
@EnableJpaRepositories(basePackages = {"com.viewnext.core.repositories"})
@EnableAspectJAutoProxy 
@Import(LogAspect.class)
public class LoginApp {

	public static void main(String[] args) {
		SpringApplication.run(LoginApp.class, args);

	}

}
