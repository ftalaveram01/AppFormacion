package com.viewnext.register.business.services;

import com.viewnext.core.business.model.Usuario;

public interface RegistroService {

	/**
	 * Metodo para registrar al usuario
	 * 
	 * @param usuario
	 * @return devuelve el id del Usuario registrado
	 */
	Long register(Usuario usuario);
	
}
