package com.viewnext.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.login.model.Usuario;

public interface LoginRepository extends JpaRepository<Usuario, Long>{
	
	Usuario findByEmailAndPassword(String email, String password);

}
