package com.viewnext.register.business.services.impl;

import com.viewnext.register.business.model.Usuario;
import com.viewnext.register.business.services.RegistroService;
import com.viewnext.register.integration.repository.UsuarioRepository;


public class RegistroServiceImpl implements RegistroService{
	
	private UsuarioRepository usuarioRepository;
	
	public RegistroServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Long register(String email, String password) {
		
		if(usuarioRepository.existsByEmail(email))
			throw new IllegalStateException("Existe un usuario con ese email.");
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setPassword(password);
		
		Usuario guardado = usuarioRepository.save(usuario);
		
		return guardado.getId();
	}
	
}
