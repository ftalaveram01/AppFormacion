package com.viewnext.login.business.services;

import java.util.Optional;

import com.viewnext.core.business.model.Usuario;

public interface LoginServices {
	
	/**
	 * Metodo para logear al usuario
	 * 
	 * @param email
	 * @param password
	 * @return devuelve un optional de Usuario, ya que puede que el Usuario sea null o no exista.
	 */
	Optional<Usuario> login(String email, String password);

}
