package com.viewnext.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.viewnext.convocatoria.ConvocatoriaApplication;
import com.viewnext.course.CourseApplication;
import com.viewnext.login.LoginApp;
import com.viewnext.register.RegisterApp;
import com.viewnext.rol.RolApplication;
import com.viewnext.usuario.UsuarioApp;

@SpringBootApplication
@ComponentScan(
	    basePackages = {
	        "com.viewnext.core",       // Escanea todo lo del core
	        "com.viewnext.course",     // Incluye lógica del módulo course
	        "com.viewnext.rol",        // Incluye lógica del módulo rol
	        "com.viewnext.convocatoria",
	        "com.viewnext.usuario",
	        "com.viewnext.register",
	        "com.viewnext.login"
	    },
	    excludeFilters = @ComponentScan.Filter(
	        type = FilterType.ASSIGNABLE_TYPE,
	        classes = {
	            CourseApplication.class, // Evita la configuración específica de course
	            RolApplication.class,     // Evita la configuración específica de rol
	            ConvocatoriaApplication.class,
	            LoginApp.class,
	            RegisterApp.class,
	            UsuarioApp.class
	        }
	    )
	)
public class MainModuleApplication {

	public static void main(String[] args) {
        SpringApplication.run(MainModuleApplication.class, args);

	}

}
