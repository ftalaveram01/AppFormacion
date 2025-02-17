package com.viewnext.login.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.core.business.model.Usuario;

/**
 * Repositorio de Usuario
 * 
 * Metodos y consultas personalizadas
 */
public interface LoginRepository extends JpaRepository<Usuario, Long>{
	
	Usuario findByEmailAndPassword(String email, String password);

}
