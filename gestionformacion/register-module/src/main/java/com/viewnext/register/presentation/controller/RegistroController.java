package com.viewnext.register.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.register.business.services.RegistroService;

@RestController
@RequestMapping("/autentificacion")
public class RegistroController {

	private RegistroService registroService;
	
	public RegistroController(RegistroService registroService) {
		this.registroService = registroService;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestParam(required = true) String email, @RequestParam(required = true) String password, UriComponentsBuilder ucb){
		
		Long id = registroService.register(email, password);
		
		return ResponseEntity.created(ucb.path("/usuarios/{id}").build(id)).build();
	}
}
