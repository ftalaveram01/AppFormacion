package com.viewnext.core.security.payloads;

import java.io.Serializable;

public class JwtResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String token;
    
	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

  
}
