package com.viewnext.usuario.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.business.services.UsuarioServices;
import com.viewnext.usuario.integration.repositories.RolRepository;
import com.viewnext.usuario.integration.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServicesImpl implements UsuarioServices{
	
	private UsuarioRepository usuarioRepository;
	
	private RolRepository rolRepository;

	public UsuarioServicesImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
		this.usuarioRepository = usuarioRepository;
		this.rolRepository = rolRepository;
	}

	@Override
	public Long create(String email, String password, RolEnum rol) {
		
		if(usuarioRepository.existsByEmail(email))
			throw new IllegalStateException("Ya existe un usuario con ese email.");
		
		Rol rolUsu = rolRepository.findByNombreRol(rol);
				
		Usuario usu = new Usuario();
		usu.setEmail(email);
		usu.setPassword(password);
		usu.setRol(rolUsu);
		
		Usuario creado = usuarioRepository.save(usu);
		
		return creado.getId();
	}

	@Override
	public void delete(Long id) {
		
		if(!usuarioRepository.existsById(id))
			throw new IllegalStateException("No existe el usuario a borrar.");
		
		usuarioRepository.deleteById(id);
	}

	@Override
	public void update(Usuario usuario) {
		
		if(!usuarioRepository.existsById(usuario.getId()))
			throw new IllegalStateException("No existe el usuario a actualizar.");
		
		usuarioRepository.save(usuario);
	}

	@Override
	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario read(Long id) {
		Optional<Usuario> usu = usuarioRepository.findById(id);
		
		if(usu.isEmpty())
			throw new IllegalStateException("No existe el usuario");
		
		return usu.get();
	}

}
