package com.viewnext.usuario.business.services;

import java.util.List;

import com.viewnext.core.business.model.Usuario;

public interface UsuarioServices {
	
	Long create(Usuario usuario, Long idAdmin);
	
	void delete(Long id, Long idAdmin);
	
	void update(Usuario usuario, Long idAdmin);
	
	List<Usuario> getAll(Long idAdmin);
	
	Usuario read(Long id, Long idAdmin);
	
	boolean isAdmin(Long idAdmin);

}
