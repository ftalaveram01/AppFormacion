package com.viewnext.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.viewnext.core.presentation.config.LogAspect;

@SpringBootApplication
@EnableAspectJAutoProxy 
@Import(LogAspect.class)
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args); 
	}

}
