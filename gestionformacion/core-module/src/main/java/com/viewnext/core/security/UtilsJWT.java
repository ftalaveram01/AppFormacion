package com.viewnext.core.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class UtilsJWT {

    private static final Logger logger = LoggerFactory.getLogger(UtilsJWT.class);
    
    @Value("${gestionformacion.tiempo.expiracion.jwt}")
    private int jwtExpirationMs;

    @Value("${gestionformacion.secreto.jwt}")
    private String jwtSecret;

    public String generarJwt(Authentication authentication) {

        UsuarioDetails usuario = (UsuarioDetails) authentication.getPrincipal();
        
        Map<String,Object> claims = new HashMap<String, Object>();
        
        claims.put("email", usuario.getUsername());
        claims.put("rol", usuario.getAuthorities());

        return Jwts.builder()
        		.setClaims(claims)
                .setSubject(usuario.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(decodificarSecreto(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Long getTokenId(String token) {

        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(decodificarSecreto())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
    
    public String getTokenUsername(String token) {

    	return Jwts.parserBuilder()
    			.setSigningKey(decodificarSecreto())
    			.build()
    			.parseClaimsJws(token)
    			.getBody()
    			.get("email", String.class);
    }

    public boolean validarJwt(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(decodificarSecreto()).build().parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        }catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("Invalid JWT Signature: {}", e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }
   
    private Key decodificarSecreto() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
