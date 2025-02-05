package com.viewnext.login.services.impl;

import java.util.Optional;

import com.viewnext.login.model.Usuario;
import com.viewnext.login.repository.LoginRepository;
import com.viewnext.login.services.LoginServices;

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
