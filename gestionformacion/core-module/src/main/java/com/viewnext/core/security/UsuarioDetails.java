package com.viewnext.core.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.viewnext.core.business.model.RolAuthority;
import com.viewnext.core.business.model.Usuario;

public class UsuarioDetails implements UserDetails{
	private static final long serialVersionUID = 1L;

	private final Usuario usuario;
	
	public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new RolAuthority(usuario.getRol()));
	}

	@Override
	public String getPassword() {
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}
	

    public Long getId() {
		return usuario.getId();
	}

}
