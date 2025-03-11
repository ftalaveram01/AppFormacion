package com.viewnext.convocatoria.model;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateRequest {
	
	private Date fechaInicio;
	
	private Date fechaFin;

}
