package com.viewnext.convocatoria.model;

import java.util.Date;

import lombok.Data;

@Data
public class ConvocatoriaRequest {
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	private Long idCurso;
	
}
