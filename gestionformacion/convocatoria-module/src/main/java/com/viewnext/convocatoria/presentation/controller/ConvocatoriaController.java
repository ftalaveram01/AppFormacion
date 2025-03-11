package com.viewnext.convocatoria.presentation.controller;

import org.springframework.web.bind.annotation.RestController;

import com.viewnext.convocatoria.business.services.ConvocatoriaServices;

@RestController
public class ConvocatoriaController {
	
	private ConvocatoriaServices convocatoriaServices;

	public ConvocatoriaController(ConvocatoriaServices convocatoriaServices) {
		this.convocatoriaServices = convocatoriaServices;
	}

}
