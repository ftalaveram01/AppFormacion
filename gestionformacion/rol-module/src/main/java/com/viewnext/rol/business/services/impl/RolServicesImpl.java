package com.viewnext.rol.business.services.impl;

import java.sql.SQLException;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.rol.business.services.RolServices;

import jakarta.transaction.Transactional;

@Service
public class RolServicesImpl implements RolServices{
	
	private RolRepository rolRepository;
	
	private UsuarioRepository usuarioRepository;
	
	public RolServicesImpl(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
		this.rolRepository = rolRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	@Override
	public Rol create(Rol rol, Long idAdmin) {
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(rol.getId() == null)
			throw new IllegalStateException("El rol tiene que tener un id valido.");
		
		if(rolRepository.existsById(rol.getId()))
			throw new IllegalStateException("Ya existe un rol con ese id.");
		
		Rol guardado = rolRepository.save(rol);
		
		return guardado;
	}

	@Transactional
	@Override
	public void delete(Long id, Long idAdmin) {
	    if(!isAdmin(idAdmin))
	        throw new IllegalStateException("No eres administrador");
	    
	    if(!rolRepository.existsById(id))
	        throw new IllegalStateException("Rol no encontrado");
	    
	    rolRepository.deleteById(id);
	}

	@Transactional
	@Override
	public Rol update(String descripcion, Long id, Long idAdmin) {
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("El rol con ID [" + id + "] no existe.");
		
		Rol rol = rolRepository.findById(id).get();
		rol.setDescripcion(descripcion);
		
		Rol creado = rolRepository.save(rol);
		return creado;
	}

	@Override
	public List<Rol> getAll() {
		return rolRepository.findAll();
	}

	@Override
	public Rol read(Long id) {
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("El rol con ID [" + id + "] no existe.");
		return rolRepository.findById(id).get();
	}
	
	private boolean isAdmin(Long idAdmin) {
		if(!usuarioRepository.existsById(idAdmin))
			throw new IllegalStateException("No existe el usuario admin");
		return usuarioRepository.isAdmin(idAdmin);
	}

}
