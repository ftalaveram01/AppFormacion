package com.viewnext.core.business.model;

import lombok.Data;

@Data
public class CourseReporte {
	
	private Long id;
	
	private String nombre;
	
	private Integer numeroHoras;
	
	private Integer numeroUsuarios;
	
	private Integer numeroConvocatorias;
	
	private String habilitado;

	public CourseReporte(Long id, String nombre, Integer numeroHoras, Integer numeroUsuarios,
			Integer numeroConvocatorias, String habilitado) {
		this.id = id;
		this.nombre = nombre;
		this.numeroHoras = numeroHoras;
		this.numeroUsuarios = numeroUsuarios;
		this.numeroConvocatorias = numeroConvocatorias;
		this.habilitado = habilitado;
	}

}
