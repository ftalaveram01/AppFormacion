package com.viewnext.login.business.services.impl;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.login.business.services.LoginServices;

/**
 * Implementa los servicios a traves del repository
 */
@Service
public class LoginServicesImpl implements LoginServices{

	private UsuarioRepository loginRepository;
	
	public LoginServicesImpl(UsuarioRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	/**
	 * Metodo para logear al usuario atraves de su email y password
	 * 
	 * Puede que no encuentre al usuario, en tal caso retorna null.
	 */
	@Override
	public Optional<Usuario> login(String email, String password) {
		Usuario usu = loginRepository.findByEmailAndPassword(email, password);
		
		if(usu != null && !usu.getHabilitado())
			throw new IllegalStateException("El usuario está deshabilitado");
		
		return Optional.ofNullable(loginRepository.findByEmailAndPassword(email, password));
	}

}
