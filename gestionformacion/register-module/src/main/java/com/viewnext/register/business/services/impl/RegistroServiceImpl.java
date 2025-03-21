package com.viewnext.register.business.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.register.business.services.RegistroService;

/**
 * Implementa los servicios a traves del repository
 */
@Service
public class RegistroServiceImpl implements RegistroService{
	
	private UsuarioRepository usuarioRepository;
	
	private RolRepository rolRepository;
	
    private PasswordEncoder passwordEncoder;
	
	public RegistroServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository
			, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.rolRepository = rolRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 
	 * Si no existe un usuario con ese email registrado
	 * Crea un nuevo usuario, lo guarda en el repositorio y devuelve el id del usuario guardado
	 * 
	 * Si existe, devuelve una excepcion
	 */
	@Override
	public Long register(Usuario usuario) {
		
		if(usuario.getId()!=null)
			throw new IllegalStateException("El usuario no puede tener id");
		
		if(usuarioRepository.existsByEmail(usuario.getEmail()))
			throw new IllegalStateException("Ya existe un usuario con ese email.");
		
		if(usuario.getRol().getId() == null) {
			
			Rol rol = rolRepository.findByNombreRol(usuario.getRol().getNombreRol());
			if(rol == null)
				throw new IllegalStateException("No existe el rol del usuario.");
			
			usuario.setRol(rol);
			
		}
		
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		
		Usuario creado = usuarioRepository.save(usuario);
		
		return creado.getId();
	}
	
}
