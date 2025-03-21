package com.viewnext.core.business.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolAuthority implements GrantedAuthority{
	
	private Rol rol;

	@Override
	public String getAuthority() {
		return "ROLE_" + rol.getNombreRol();
	}

}
