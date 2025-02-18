package com.viewnext.usuario.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.business.services.UsuarioServices;
import com.viewnext.usuario.integration.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServicesImpl implements UsuarioServices{
	
	private UsuarioRepository usuarioRepository;

	public UsuarioServicesImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	@Override
	public Long create(Usuario usuario, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(usuarioRepository.existsByEmail(usuario.getEmail()))
			throw new IllegalStateException("Ya existe un usuario con ese email.");
		
		Usuario creado = usuarioRepository.save(usuario);
		
		return creado.getId();
	}

	@Transactional
	@Override
	public void delete(Long id, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(!usuarioRepository.existsById(id))
			throw new IllegalStateException("No existe el usuario a borrar.");
		
		usuarioRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void update(Usuario usuario, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(!usuarioRepository.existsById(usuario.getId()))
			throw new IllegalStateException("No existe el usuario a actualizar.");
		
		usuarioRepository.save(usuario);
	}

	@Override
	public List<Usuario> getAll(Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No existe eres administrador");
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario read(Long id, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		Optional<Usuario> usu = usuarioRepository.findById(id);
		
		if(usu.isEmpty())
			throw new IllegalStateException("No existe el usuario");
		
		return usu.get();
	}
	
	public boolean isAdmin(Long idAdmin) {
		if(!usuarioRepository.existsById(idAdmin))
			throw new IllegalStateException("No existe el usuario admin");
		Usuario usuario = usuarioRepository.findById(idAdmin).get();
		if(usuario.getRol().getNombreRol().equals(RolEnum.ADMIN))
			return true;
		return false;
	}

}
