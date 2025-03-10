package com.viewnext.register.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;

public interface RolRepository extends JpaRepository<Rol, Long> {

	Rol findByNombreRol(String nombreRol);
}
