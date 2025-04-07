package com.viewnext.core.business.model;

import lombok.Data;

@Data
public class UsuarioReporte {

	private Long id;
		
	 private String email;
	    
	 private String rol;
	    
	 private String habilitado;
	 
		
	 public UsuarioReporte(Long id, String email, String rol, String habilitado) {
		this.id = id;
		this.email = email;
		this.rol = rol;
		this.habilitado = habilitado;
	}

}
