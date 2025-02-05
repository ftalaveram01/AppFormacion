package com.viewnext.login.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viewnext.login.model.Usuario;
import com.viewnext.login.services.LoginServices;


@RestController
@RequestMapping("/autentificacion")
public class LoginController {
	
	private LoginServices loginServices;
	
	public LoginController(LoginServices loginServices) {
		this.loginServices = loginServices;
	}

	@GetMapping("/login")
	public ResponseEntity<?> login(@RequestParam(required = true) String email, @RequestParam(required = true) String password){
		
		Optional<Usuario> usuario = loginServices.login(email, password);
		
		if(usuario.isEmpty())
			throw new IllegalStateException("Login incorrecto.");
		
		return ResponseEntity.ok(usuario);
	}

}
