package com.viewnext.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.business.model.UsuarioReporte;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmailAndPassword(String email, String password);
	
	Usuario findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	@Query("SELECT new com.viewnext.core.business.model.UsuarioReporte(u.id, u.email, u.rol.nombreRol, CASE WHEN u.habilitado = true THEN 'Si' ELSE 'No' END) FROM Usuario u")
	List<UsuarioReporte> findAllUsuarioReportes();
	
}
