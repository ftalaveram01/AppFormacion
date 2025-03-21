package com.viewnext.convocatoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.viewnext.core.presentation.config.LogAspect;

@ComponentScan(basePackages = {"com.viewnext.core.presentation.config", "com.viewnext.convocatoria",
"com.viewnext.core.security"})
@EntityScan(basePackages = {"com.viewnext.core.business.model"})
@EnableJpaRepositories(basePackages = {"com.viewnext.core.repositories"})
@SpringBootApplication
@EnableAspectJAutoProxy 
@Import(LogAspect.class)
@EnableScheduling
public class ConvocatoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvocatoriaApplication.class, args); 
	}
	
}
