package com.viewnext.convocatoria.model;

import java.util.Date;

import lombok.Data;

public class UpdateRequest {
	
	private Date fechaInicio;
	
	private Date fechaFin;

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

}
