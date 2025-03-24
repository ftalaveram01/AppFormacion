package com.viewnext.core.security;

import java.io.IOException;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

public class FiltroAutentificacionJWT extends OncePerRequestFilter{
	
	 @Autowired
	 private UtilsJWT utilsJwt;
	 
	 @Autowired
	 private UsuarioDetailsService usuarioDetailsService;
	 
	 private static final List<String> RUTAS_PUBLICAS = List.of(
	            "/autentificacion/");
	 
	 @Override
	 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		 
	 	if(! RUTAS_PUBLICAS.stream().anyMatch(request.getServletPath()::startsWith)) {
			 try {
				 
				 String jwt = parseJwt(request);
				 
				 if (jwt != null && utilsJwt.validarJwt(jwt)) {
					 
					 UsuarioDetails usuario = (UsuarioDetails) usuarioDetailsService.loadUserByUsername(utilsJwt.getTokenUsername(jwt));

					 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
					 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					 SecurityContextHolder.getContext().setAuthentication(authentication);
				 }
			 } catch (Exception e) {
				 logger.error("Cannot set user authentication: {}", e);
			 }
	 	}
		
		 
		 filterChain.doFilter(request, response);
	 }
	 
	 

	 private String parseJwt(HttpServletRequest request) {
	    	
		String token = request.getHeader("Authorization"); 
	        
		if (token != null && token.startsWith("Bearer ")) {
			 return token.substring(7);
		}
		
		return null;
	}
}
