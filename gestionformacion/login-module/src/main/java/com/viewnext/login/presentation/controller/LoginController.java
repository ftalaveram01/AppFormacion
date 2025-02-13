package com.viewnext.login.presentation.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viewnext.login.business.model.Usuario;
import com.viewnext.login.business.services.LoginServices;

/**
 * Controlador REST para gestionar el login de los usuarios
 */
@RestController
@RequestMapping("/autentificacion")
public class LoginController {
	
	private LoginServices loginServices;
	
	public LoginController(LoginServices loginServices) {
		this.loginServices = loginServices;
	}

	/**
	 * 
     * Este metodo permite a un usuario iniciar sesion por su email y contraseña
     * Si son incorrectos, lanza una exception
     *
     * @param email
     * @param password
     * @return una ResponseEntity que contiene la información del usuario si el inicio de sesión es exitoso
     */
	@GetMapping("/login")
	public ResponseEntity<Optional<Usuario>> login(@RequestParam(required = true) String email, @RequestParam(required = true) String password){
		
		Optional<Usuario> usuario = loginServices.login(email, password);
		
		if(usuario.isEmpty())
			throw new IllegalStateException("Login incorrecto.");
		
		return ResponseEntity.ok(usuario);
	}

}
