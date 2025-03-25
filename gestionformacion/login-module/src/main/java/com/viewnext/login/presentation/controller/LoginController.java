package com.viewnext.login.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viewnext.core.security.UtilsJWT;
import com.viewnext.core.security.payloads.JwtResponse;
import com.viewnext.login.business.services.LoginServices;

/**
 * Controlador REST para gestionar el login de los usuarios
 */
@RestController
@RequestMapping("/autentificacion")
public class LoginController {
	
    private AuthenticationManager authenticationManager;
    private UtilsJWT utilsJwt;
	
	public LoginController(AuthenticationManager authenticationManager,
			UtilsJWT utilsJwt) {
		this.authenticationManager = authenticationManager;
		this.utilsJwt = utilsJwt;
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
	public ResponseEntity<JwtResponse> login(@RequestParam(required = true) String email, @RequestParam(required = true) String password, @RequestParam(required = true)String otpCode){
		
    	Authentication autenticacion = null;
    	
    	try {
    		autenticacion = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    	} catch(Exception e) {
    		throw new IllegalStateException("Usuario inválido");
    	}
        
        SecurityContextHolder.getContext().setAuthentication(autenticacion);
        String jwt = utilsJwt.generarJwt(autenticacion);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

}
