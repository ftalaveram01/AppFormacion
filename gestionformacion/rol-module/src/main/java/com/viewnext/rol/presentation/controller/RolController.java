package com.viewnext.rol.presentation.controller;

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

import com.viewnext.core.business.model.Rol;
import com.viewnext.rol.business.services.RolServices;

@RestController
@RequestMapping("/roles")
public class RolController {
	
	private RolServices rolServices;

	public RolController(RolServices rolServices) {
		this.rolServices = rolServices;
	}
	
	@GetMapping
	public List<Rol> getAll(){
		return rolServices.getAll();
	}
	
	@PostMapping
	public Rol createRol(@RequestBody Rol rol, @RequestParam Long idAdmin) {
		return rolServices.create(rol, idAdmin);
	}
	
	@GetMapping("/{id}")
	public Rol read(@PathVariable Long id) {
		return rolServices.read(id);
	}
	
	@PutMapping("/{id}")
	public Rol update(@PathVariable Long id, @RequestBody Rol rol, @RequestParam Long idAdmin) {
		return rolServices.update(rol, id, idAdmin);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long idAdmin){
		rolServices.delete(id, idAdmin);
		return ResponseEntity.noContent().build();
	}
	
	

}
