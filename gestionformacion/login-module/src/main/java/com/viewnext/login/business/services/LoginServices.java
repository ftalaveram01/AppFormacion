package com.viewnext.login.business.services;

import java.util.Optional;

import com.viewnext.login.business.model.Usuario;

public interface LoginServices {
	
	Optional<Usuario> login(String email, String password);

}
