package com.viewnext.convocatoria.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;

@RestController
@RequestMapping("/convocatorias")
public class ConvocatoriaController {
	
	private ConvocatoriaServices convocatoriaServices;

	public ConvocatoriaController(ConvocatoriaServices convocatoriaServices) {
		this.convocatoriaServices = convocatoriaServices;
	}
	
	@PostMapping
	public Convocatoria create(@RequestBody ConvocatoriaRequest request, @RequestParam Long idAdmin) {
		return convocatoriaServices.create(idAdmin, request);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestParam Long idAdmin, @RequestBody UpdateRequest request){
		convocatoriaServices.update(id, idAdmin, request);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long idAdmin){
		convocatoriaServices.delete(id, idAdmin);
		return ResponseEntity.noContent().build();
	}
	

}
