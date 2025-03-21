package com.viewnext.usuario.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.business.services.UsuarioServices;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private UsuarioServices usuarioServices;

	public UsuarioController(UsuarioServices usuarioServices) {
		this.usuarioServices = usuarioServices;
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<String> create(@RequestBody Usuario usuario, UriComponentsBuilder ucb){
		Long id = usuarioServices.create(usuario);
		
		return ResponseEntity.created(ucb.path("/usuarios/{id}").build(id)).build();
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		usuarioServices.delete(id);
		return ResponseEntity.noContent().build();
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/actualizar")
	public ResponseEntity<Void> update(@RequestBody Usuario usuario){
		usuarioServices.update(usuario);
		return ResponseEntity.noContent().build();
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public List<Usuario> getAll(){
		return usuarioServices.getAll();
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public Usuario read(@PathVariable Long id) {
		return usuarioServices.read(id);
	}
	
	@DeleteMapping("/deshabilitar")
	public ResponseEntity<Void> deshabilitar(@RequestParam String email){
		usuarioServices.deshabilitarUsuario(email);
		return ResponseEntity.noContent().build();
	}
	
	

}
