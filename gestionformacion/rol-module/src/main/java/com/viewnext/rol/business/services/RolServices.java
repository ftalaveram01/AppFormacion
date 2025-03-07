package com.viewnext.rol.business.services;

import java.util.List;

import com.viewnext.core.business.model.Rol;


public interface RolServices {
	
	Rol create(Rol rol, Long idAdmin);
	
	void delete(Long id, Long idAdmin);
	
	Rol update(Rol rol, Long id, Long idAdmin);
	
	List<Rol> getAll();
	
	Rol read(Long id);

}
