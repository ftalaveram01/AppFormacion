package com.viewnext.convocatoria.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
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

}
