package com.viewnext.register.business.services;

public interface RegistroService {

	/**
	 * Metodo para registrar al usuario
	 * 
	 * @param email
	 * @param password
	 * @return devuelve el id del Usuario registrado
	 */
	Long register(String email, String password);
	
}
