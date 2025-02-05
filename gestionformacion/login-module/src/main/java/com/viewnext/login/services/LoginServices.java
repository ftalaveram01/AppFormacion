package com.viewnext.login.services;

import java.util.Optional;

import com.viewnext.login.model.Usuario;

public interface LoginServices {
	
	Optional<Usuario> login(String email, String password);

}
