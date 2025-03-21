package com.viewnext.rol.business.services;

import java.util.List;

import com.viewnext.core.business.model.Rol;


public interface RolServices {
	
	Rol create(Rol rol);
	
	void delete(Long id);
	
	Rol update(String descripcion, Long id);
	
	List<Rol> getAll();
	
	Rol read(Long id);

}
