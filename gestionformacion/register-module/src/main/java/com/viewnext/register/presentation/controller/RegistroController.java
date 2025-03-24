package com.viewnext.register.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.security.UtilsOTP;
import com.viewnext.core.security.payloads.RegistroResponse;
import com.viewnext.register.business.services.RegistroService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 * Controlador REST para gestionar el registro de los usuarios
 */
@RestController
@RequestMapping("/autentificacion")
public class RegistroController {

	private RegistroService registroService;
	
	private UtilsOTP utilsOTP;
	
	public RegistroController(RegistroService registroService, UtilsOTP utilsOTP) {
		this.registroService = registroService;
		this.utilsOTP = utilsOTP;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<RegistroResponse> registrar(@RequestBody Usuario usuario, UriComponentsBuilder ucb){
		
        GoogleAuthenticatorKey key = utilsOTP.generateKey();
        usuario.setSecreto(key.getKey());
        registroService.register(usuario);
        String qr = utilsOTP.generateQRCode(usuario.getEmail(), key);
        return ResponseEntity.ok(new RegistroResponse(qr, key.getKey()));
	}
}
