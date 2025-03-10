package com.viewnext.course.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmailAndPassword(String email, String password);
	
	Boolean existsByEmail(String email);
	
	@Query("SELECT u.rol.nombreRol = 'ADMIN' FROM Usuario u WHERE u.id = :id")
	Boolean isAdmin(Long id);
	
}
