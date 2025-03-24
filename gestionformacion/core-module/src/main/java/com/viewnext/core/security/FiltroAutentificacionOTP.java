package com.viewnext.core.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.viewnext.core.business.model.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FiltroAutentificacionOTP extends OncePerRequestFilter{
	
	@Autowired
	private UtilsOTP utilsOTP;
	
    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    
	    if (request.getRequestURI().equals("/autentificacion/login")) {
	        String otpCode = request.getParameter("otpCode");
	        String email = request.getParameter("email");
	        
	        try {
	            int otpCodeInt = Integer.parseInt(otpCode);
	            UsuarioDetails usuario = (UsuarioDetails) usuarioDetailsService.loadUserByUsername(email);
	            if (usuario == null || !utilsOTP.verifyCode(usuario.getSecreto(), otpCodeInt)) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código OTP no válido");
	                return;
	            }
	        } catch (NumberFormatException e) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de código OTP no válido");
	            return;
	        }
	    }
	    
	    filterChain.doFilter(request, response);
	}

}
