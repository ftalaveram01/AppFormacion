package com.viewnext.register.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
	
	/**
     * 
     * Este metodo permite registrar un nuevo usuario por su email y contraseña
     * Devuelve una ResponseEntity con la URI del nuevo usuario registrado
     *
     * @param email
     * @param password
     * @param ucb un UriComponentsBuilder para construir la URI del recurso creado
     * @return una ResponseEntity con la URI del nuevo usuario registrado
     */
	@PostMapping("/registrar")
	public ResponseEntity<Void> registrar(@RequestParam(required = true) String email, @RequestParam(required = true) String password, UriComponentsBuilder ucb){
		
		Long id = registroService.register(email, password);
		
		return ResponseEntity.created(ucb.path("/usuarios/{id}").build(id)).build();
	}
}
