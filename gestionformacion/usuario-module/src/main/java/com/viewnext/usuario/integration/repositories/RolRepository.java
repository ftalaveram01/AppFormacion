package com.viewnext.usuario.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;

public interface RolRepository extends JpaRepository<Rol, Long> {

	Rol findByNombreRol(RolEnum nombreRol);
}
