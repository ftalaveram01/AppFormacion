package com.viewnext.register.business.services.impl;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.register.business.services.RegistroService;
import com.viewnext.register.integration.repository.UsuarioRepository;

/**
 * Implementa los servicios a traves del repository
 */
@Service
public class RegistroServiceImpl implements RegistroService{
	
	private UsuarioRepository usuarioRepository;
	
	public RegistroServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Metodo para registrar al usuario a traves de su email y password
	 * 
	 * Si no existe un usuario con ese email registrado
	 * Crea un nuevo usuario, lo guarda en el repositorio y devuelve el id del usuario guardado
	 * 
	 * Si existe, devuelve una excepcion
	 */
	@Override
	public Long register(String email, String password, Rol rol) {
		
		if(usuarioRepository.existsByEmail(email))
			throw new IllegalStateException("Existe un usuario con ese email.");
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setPassword(password);
		usuario.setRol(rol);
		
		Usuario guardado = usuarioRepository.save(usuario);
		
		return guardado.getId();
	}
	
}
