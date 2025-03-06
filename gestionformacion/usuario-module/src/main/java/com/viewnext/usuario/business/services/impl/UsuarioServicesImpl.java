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

	@Transactional
	@Override
	public Long create(Usuario usuario, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
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
		
		Usuario usu = usuarioRepository.findById(id).get();
		
		if(usu.getHabilitado()) {
			usu.setHabilitado(false);
			usuarioRepository.save(usu);
		}else
			throw new IllegalStateException("El usuario ya está deshabilitado");
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
	
	@Transactional
	@Override
	public void deshabilitarUsuario(String email) {
		
		if(!usuarioRepository.existsByEmail(email))
			throw new IllegalStateException("No existe el usuario.");
		
		Usuario usu = usuarioRepository.findByEmail(email);
		
		if(!usu.getHabilitado())
			throw new IllegalStateException("El usuario ya está deshabilitado");
		usu.setHabilitado(false);
		
		usuarioRepository.save(usu);
		
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
