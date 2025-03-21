package com.viewnext.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.viewnext.core.presentation.config.LogAspect;

@ComponentScan(basePackages = {"com.viewnext.core.presentation.config", "com.viewnext.course",
		"com.viewnext.core.security"})
@EntityScan(basePackages = {"com.viewnext.core.business.model"})
@EnableJpaRepositories(basePackages = {"com.viewnext.core.repositories"})
@SpringBootApplication
@EnableAspectJAutoProxy 
@Import(LogAspect.class)
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args); 
	}

}
