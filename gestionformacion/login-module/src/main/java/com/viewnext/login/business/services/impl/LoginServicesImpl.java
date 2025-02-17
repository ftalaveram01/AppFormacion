package com.viewnext.login.business.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.login.business.services.LoginServices;
import com.viewnext.login.integration.repository.LoginRepository;

/**
 * Implementa los servicios a traves del repository
 */
@Service
public class LoginServicesImpl implements LoginServices{

	private LoginRepository loginRepository;
	
	public LoginServicesImpl(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	/**
	 * Metodo para logear al usuario atraves de su email y password
	 * 
	 * Puede que no encuentre al usuario, en tal caso retorna null.
	 */
	@Override
	public Optional<Usuario> login(String email, String password) {
		return Optional.ofNullable(loginRepository.findByEmailAndPassword(email, password));
	}

}
