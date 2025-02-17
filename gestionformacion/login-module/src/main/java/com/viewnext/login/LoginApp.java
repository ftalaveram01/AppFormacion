package com.viewnext.login;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.viewnext.core.business.model"})
public class LoginApp {

	public static void main(String[] args) {
		SpringApplication.run(LoginApp.class, args);

	}

}
