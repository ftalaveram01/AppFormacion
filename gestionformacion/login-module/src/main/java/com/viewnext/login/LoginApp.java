package com.viewnext.login;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.viewnext.core.presentation.config.LogAspect;

@SpringBootApplication
@EntityScan(basePackages = {"com.viewnext.core.business.model"})
@EnableAspectJAutoProxy 
@Import(LogAspect.class)
public class LoginApp {

	public static void main(String[] args) {
		SpringApplication.run(LoginApp.class, args);

	}

}
