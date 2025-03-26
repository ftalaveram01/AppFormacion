package com.viewnext.rol.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolAuthority;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.core.security.UsuarioDetails;
import com.viewnext.core.security.UsuarioDetailsService;
import com.viewnext.core.security.UtilsJWT;
import com.viewnext.rol.business.services.RolServices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RolControllerTest {
	
	 @LocalServerPort
	 private Integer port;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RolServices rolServices;
	
    @MockitoBean
    private UtilsJWT utilsJWT;
    
    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;
    
    @MockitoBean
    private UsuarioRepository usuarioRepository;
	
    @Value("${gestionformacion.secreto.jwt}")
    private String jwtSecret;
    
    @Value("${gestionformacion.tiempo.expiracion.jwt}")
    private int jwtExpirationMs;
	
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0")
    		.withDatabaseName("gestionformacion")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("schema.sql");;
    
    @BeforeAll
    static void beforeAll() {
    	mysqlContainer.start();
    }

    @AfterAll
    static void afterAll() {
    	mysqlContainer.stop();
    }
    
    @BeforeEach
    void setUp() {
      RestAssured.baseURI = "http://localhost:" + port;
    }
	
	@DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
      registry.add("spring.datasource.username", mysqlContainer::getUsername);
      registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
	
	@Test
	void testGetAll() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	Rol rol2 = new Rol();
    	rol2.setId(5L);
    	rol2.setNombreRol("ALUMNO");
    	rol2.setDescripcion("Prueba2");
    	
    	when(rolServices.getAll()).thenReturn(Arrays.asList(rol, rol2));
    	
		mockMvc.perform(get("/roles")
                .with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))))
                .andExpect(status().isOk());
		
	}

	
	 @Test
	 void testCreateRol() throws Exception {
		 
		 this.mockAdmin();
		 
		 String jwtToken = "Bearer " + this.generateJwtAdmin();

		 mockMvc.perform(post("/roles")
	                .contentType("application/json")
	                .content("{\"nombre\":\"ROLE_USER\"}")
	                .header("Authorization", jwtToken))
	                .andExpect(status().isOk());
	  }
	
	@Test
	void testCreateNoAdmin() throws Exception{
		
		this.mockAlumno();
		
		 String jwtToken = "Bearer " + this.generateJwtAlumno();

		 mockMvc.perform(post("/roles")
	                .contentType("application/json")
	                .content("{\"nombre\":\"ROLE_USER\"}")
	                .header("Authorization", jwtToken))
	                .andExpect(status().isUnauthorized());
	}
	
	@Test
	void testRead() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	when(rolServices.read(5L)).thenReturn(rol);
    	
		mockMvc.perform(get("/roles/5")
                .with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))))
                .andExpect(status().isOk());
	}
	
	@Test
	void testReadError() throws Exception{
		when(rolServices.read(5L)).thenThrow(new IllegalStateException("Excepcion de read"));
		
		mockMvc.perform(get("/roles/5")
                .with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	void testUpdate() throws Exception{
		
		this.mockAdmin();
		 
		String jwtToken = "Bearer " + this.generateJwtAdmin();
    	
    	mockMvc.perform(put("/roles/1")
                .contentType("application/json")
                .content("{\"descripcion\":\"Actualizando rol\"}")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk());
	}
	
	@Test
	void testUpdateNoAdmin() throws Exception{

		this.mockAlumno();
		 
		String jwtToken = "Bearer " + this.generateJwtAlumno();
    	
    	mockMvc.perform(put("/roles/1")
                .contentType("application/json")
                .content("{\"descripcion\":\"Actualizando rol\"}")
                .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDelete() throws Exception{
		
		this.mockAdmin();
		 
		String jwtToken = "Bearer " + this.generateJwtAdmin();
    	
    	mockMvc.perform(delete("/roles/1")
                .contentType("application/json")
                .header("Authorization", jwtToken))
                .andExpect(status().isNoContent());
	}
	
	@Test
	void testDeleteNoAdmin() throws Exception{
		
		this.mockAlumno();
		 
		String jwtToken = "Bearer " + this.generateJwtAlumno();
    	
    	mockMvc.perform(delete("/roles/1")
                .contentType("application/json")
                .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
	}
	
    private String generateJwtAdmin() {
        Rol admin = new Rol();
        admin.setNombreRol("ADMIN");
        return Jwts.builder()
                .claim("email", "example100@example.com")
                .claim("rol", Collections.singleton(new RolAuthority(admin)))
                .setSubject("1")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(decodificarSecreto())
                .compact();
    }

    private String generateJwtAlumno() {
        Rol alumno = new Rol();
        alumno.setNombreRol("ALUMNO");
        return Jwts.builder()
                .claim("email", "example100@example.com")
                .claim("rol", Collections.singleton(new RolAuthority(alumno)))
                .setSubject("1")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(decodificarSecreto())
                .compact();
    }
    
    private void mockAdmin() {
    	Rol admin = new Rol();
	     admin.setNombreRol("ADMIN");
	     
	     Usuario user = new Usuario(1L, "example100@example.com", "secreto", "password", admin, true);
		 
		 UsuarioDetails usuarioDetails = new UsuarioDetails(user);
		 when(usuarioRepository.existsByEmail("example100@example.com")).thenReturn(true);
	     when(usuarioRepository.findByEmail("example100@example.com")).thenReturn(user);
	     when(usuarioDetailsService.loadUserByUsername("example100@example.com")).thenReturn(usuarioDetails);
	     when(utilsJWT.validarJwt(any(String.class))).thenReturn(true);
	     when(utilsJWT.getTokenUsername(any(String.class))).thenReturn("example100@example.com");
    }
    
    private void mockAlumno() {
    	Rol alumno = new Rol();
	     alumno.setNombreRol("ALUMNO");
	     
	     Usuario user = new Usuario(1L, "example100@example.com", "secreto", "password", alumno, true);
		 
		 UsuarioDetails usuarioDetails = new UsuarioDetails(user);
		 when(usuarioRepository.existsByEmail("example100@example.com")).thenReturn(true);
	     when(usuarioRepository.findByEmail("example100@example.com")).thenReturn(user);
	     when(usuarioDetailsService.loadUserByUsername("example100@example.com")).thenReturn(usuarioDetails);
	     when(utilsJWT.validarJwt(any(String.class))).thenReturn(true);
	     when(utilsJWT.getTokenUsername(any(String.class))).thenReturn("example100@example.com");
    }
    
    private Key decodificarSecreto() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
