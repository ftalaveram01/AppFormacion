package com.viewnext.convocatoria.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viewnext.core.business.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u.rol.nombreRol = 'ADMIN' FROM Usuario u WHERE u.id = :id")
	Boolean isAdmin(Long id);
	
}
