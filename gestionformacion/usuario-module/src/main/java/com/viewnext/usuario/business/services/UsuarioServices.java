package com.viewnext.usuario.business.services;

import java.util.List;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;

public interface UsuarioServices {
	
	Long create(String email, String password, Rol rol);
	
	void delete(Long id);
	
	void update(Usuario usuario);
	
	List<Usuario> getAll();
	
	Usuario read(Long id);

}
