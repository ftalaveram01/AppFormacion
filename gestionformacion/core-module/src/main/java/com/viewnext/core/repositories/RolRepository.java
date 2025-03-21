package com.viewnext.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.core.business.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long>{

	Rol findByNombreRol(String nombreRol);
}
