package com.viewnext.convocatoria.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

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
	public ResponseEntity<Convocatoria> create(@RequestBody ConvocatoriaRequest request, @RequestParam Long idAdmin) {
	    Convocatoria convocatoria = convocatoriaServices.create(idAdmin, request);
	    return ResponseEntity.ok(convocatoria);
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
	

	@GetMapping
	public ResponseEntity<List<Convocatoria>> getAll(@RequestParam(required = true) Long idAdmin) {
	    List<Convocatoria> convocatorias = convocatoriaServices.getAll(idAdmin);
	    return ResponseEntity.ok(convocatorias);
	}

	@GetMapping("/activas")
	public ResponseEntity<List<Convocatoria>> getActivas() {
	    List<Convocatoria> activas = convocatoriaServices.getActivas();
	    return ResponseEntity.ok(activas);
	}

	@GetMapping("/usuario/{idUsuario}")
	public ResponseEntity<List<Convocatoria>> getFromUsuario(@PathVariable Long idUsuario) {
	    List<Convocatoria> convocatorias = convocatoriaServices.getFromUsuario(idUsuario);
	    return ResponseEntity.ok(convocatorias);
	}

	@PutMapping("/{idConvocatoria}/inscribir")
	public ResponseEntity<String> inscribir(@RequestParam(required = true) Long idUsuario, @PathVariable(required = true) Long idConvocatoria) {
	    convocatoriaServices.inscribirUsuario(idUsuario, idConvocatoria);
	    return ResponseEntity.ok().build();
	}
	
	@PostMapping("/{idConvocatoria}/usuarios/certificado")
	public ResponseEntity<String> enviarCertificado(@PathVariable(required = true) Long idConvocatoria, @RequestParam(required = true) Long idUsuario){
		convocatoriaServices.generarCertificado(idConvocatoria, idUsuario);
		return ResponseEntity.ok().build();
	}
}
