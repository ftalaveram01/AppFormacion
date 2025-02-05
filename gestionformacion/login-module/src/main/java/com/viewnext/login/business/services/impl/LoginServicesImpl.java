package com.viewnext.login.business.services.impl;

import java.util.Optional;

import com.viewnext.login.business.model.Usuario;
import com.viewnext.login.business.services.LoginServices;
import com.viewnext.login.integration.repository.LoginRepository;

public class LoginServicesImpl implements LoginServices{

	private LoginRepository loginRepository;
	
	public LoginServicesImpl(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Override
	public Optional<Usuario> login(String email, String password) {
		return Optional.ofNullable(loginRepository.findByEmailAndPassword(email, password));
	}

}
