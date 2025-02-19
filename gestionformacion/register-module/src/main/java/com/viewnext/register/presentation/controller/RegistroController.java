package com.viewnext.register.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.register.business.services.RegistroService;

/**
 * Controlador REST para gestionar el registro de los usuarios
 */
@RestController
@RequestMapping("/autentificacion")
public class RegistroController {

	private RegistroService registroService;
	
	public RegistroController(RegistroService registroService) {
		this.registroService = registroService;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<Void> registrar(@RequestBody Usuario usuario, UriComponentsBuilder ucb){
		
		Long id = registroService.register(usuario);
		
		return ResponseEntity.created(ucb.path("/usuarios/{id}").build(id)).build();
	}
}
