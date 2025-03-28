package com.viewnext.core.business.model;

import java.util.Date;

import lombok.Data;

@Data
public class ConvocatoriaReporte {
	
	Long id;
	
	Date fechaInicio;
	
	Date fechaFin;
	
	String estado;
	
	String curso;
	
	Integer usuariosInscritos;

	public ConvocatoriaReporte(Long id, Date fechaInicio, Date fechaFin, String estado, String curso,
			Integer usuariosInscritos) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
		this.curso = curso;
		this.usuariosInscritos = usuariosInscritos;
	}
	
	

}
