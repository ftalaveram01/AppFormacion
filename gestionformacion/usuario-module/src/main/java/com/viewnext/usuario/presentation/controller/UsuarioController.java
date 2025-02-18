package com.viewnext.usuario.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.business.services.UsuarioServices;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private UsuarioServices usuarioServices;

	public UsuarioController(UsuarioServices usuarioServices) {
		this.usuarioServices = usuarioServices;
	}
	
	@PostMapping("/crear")
	public ResponseEntity<String> create(@RequestBody Usuario usuario, @RequestParam Long idAdmin, UriComponentsBuilder ucb){
		Long id = usuarioServices.create(usuario, idAdmin);
		
		return ResponseEntity.created(ucb.path("/usuarios/{id}").build(id)).build();
	}
	
	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long idAdmin){
		usuarioServices.delete(id, idAdmin);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<Void> update(@RequestBody Usuario usuario, @RequestParam Long idAdmin){
		usuarioServices.update(usuario, idAdmin);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	public List<Usuario> getAll(@RequestParam Long idAdmin){
		return usuarioServices.getAll(idAdmin);
	}
	
	@GetMapping("/{id}")
	public Usuario read(@PathVariable Long id, @RequestParam Long idAdmin) {
		return usuarioServices.read(id, idAdmin);
	}
	
	

}
