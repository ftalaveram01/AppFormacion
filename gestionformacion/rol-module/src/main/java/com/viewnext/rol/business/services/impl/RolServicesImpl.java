package com.viewnext.rol.business.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.rol.business.services.RolServices;
import com.viewnext.rol.integration.repositories.RolRepository;

import jakarta.transaction.Transactional;

@Service
public class RolServicesImpl implements RolServices{
	
	private RolRepository rolRepository;
	
	public RolServicesImpl(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}

	@Transactional
	@Override
	public Rol create(Rol rol) {
		if(rol.getId() == null)
			throw new IllegalStateException("El rol tiene que tener un id asignado.");
		
		if(rolRepository.existsById(rol.getId()))
			throw new IllegalStateException("Ya existe un rol con ese id.");
		
		Rol guardado = rolRepository.save(rol);
		
		return guardado;
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("Rol no encontrado");
		
		rolRepository.deleteById(id);
	}

	@Transactional
	@Override
	public Rol update(Rol rol, Long id) {
		if(rol.getId()==null)
			rol.setId(id);
		else if(rol.getId() != id)
			throw new IllegalStateException("No coincide el id del body con la ruta");
		
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("El rol con ID [" + id + "] no existe.");
		
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

}
