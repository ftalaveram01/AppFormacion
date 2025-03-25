package com.viewnext.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService{
	
	private UsuarioRepository usuarioRepository;
	
	public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(Boolean.FALSE.equals(usuarioRepository.existsByEmail(username)))
			throw new UsernameNotFoundException("No existe el usuario");

		Usuario usuario = usuarioRepository.findByEmail(username);
	
		return new UsuarioDetails(usuario);
	}

}
