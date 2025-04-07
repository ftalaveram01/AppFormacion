package com.viewnext.usuario.business.services;

import java.util.List;

import com.viewnext.core.business.model.Usuario;

public interface UsuarioServices {
	
	Long create(Usuario usuario);
	
	void delete(Long id);
	
	void update(Usuario usuario);
	
	List<Usuario> getAll();
	
	Usuario read(Long id);
	
	void deshabilitarUsuario(String email);
	
	byte[] generarReporte();

}
