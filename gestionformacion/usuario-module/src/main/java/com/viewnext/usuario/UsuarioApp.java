package com.viewnext.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"com.viewnext.core.business.model"})
@SpringBootApplication
public class UsuarioApp {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApp.class, args);

	}

}
