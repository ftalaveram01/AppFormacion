package com.viewnext.convocatoria.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolAuthority;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.core.security.UsuarioDetails;
import com.viewnext.core.security.UsuarioDetailsService;
import com.viewnext.core.security.UtilsJWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ConvocatoriaControllerTest {
	
	@LocalServerPort
	private Integer port;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
    @MockitoBean
    private UtilsJWT utilsJWT;
    
    @MockitoBean
    private UsuarioRepository usuarioRepository;
    
    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;
	
    @Value("${gestionformacion.secreto.jwt}")
    private String jwtSecret;
    
    @Value("${gestionformacion.tiempo.expiracion.jwt}")
    private int jwtExpirationMs;

	@MockitoBean
	private ConvocatoriaServices convocatoriaServices;
	
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
		
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getAll()).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias").contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testGetAllNoAdmin() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		mockMvc.perform(get("/convocatorias").contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetActivas() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getActivas()).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias/activas").contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testGetFromUsuario() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getFromUsuario(2L)).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias/usuario/2")
				.contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testInscribir() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		mockMvc.perform(put("/convocatorias/2/inscribir").contentType("application/json")
				.param("idUsuario", "5")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).inscribirUsuario(2L, 5L);
	}
	
	@Test
	void testInscribirError() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .inscribirUsuario(2L, 5L);
		
		mockMvc.perform(put("/convocatorias/2/inscribir").contentType("application/json")
				.param("idUsuario", "5")
				.header("Authorization", jwtToken))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testEnviarCertificado() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		mockMvc.perform(post("/convocatorias/2/usuarios/certificado").contentType("application/json")
				.param("idUsuario", "5")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).generarCertificado(5L, 2L);
	}
	
	
	@Test
	void testEnviarCertificadoError() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .generarCertificado(5L, 2L);
		
		mockMvc.perform(post("/convocatorias/2/usuarios/certificado").contentType("application/json")
				.param("idUsuario", "5")
				.header("Authorization", jwtToken))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testDelete() throws Exception{
		
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		mockMvc.perform(delete("/convocatorias/2")
				.contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).delete(2L);
	}
	
	@Test
	void testDeleteNoAdmin() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		mockMvc.perform(delete("/convocatorias/2")
				.contentType("application/json")
				.header("Authorization", jwtToken))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}
	
	@Test
	void testUpdate() throws Exception{
		
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
	    String json = mapper.writeValueAsString(new UpdateRequest());
		
		mockMvc.perform(put("/convocatorias/2").contentType("application/json")
				.content(json)
				.header("Authorization", jwtToken))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).update(2L, new UpdateRequest());
	}
	
	@Test
	void testUpdateExcepcion() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
	    
	    String json = mapper.writeValueAsString(new UpdateRequest());
		
		mockMvc.perform(put("/convocatorias/2").contentType("application/json")
				.content(json)
				.header("Authorization", jwtToken))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}
	
	@Test
	void testCreate() throws Exception{
		
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
	    
		when(convocatoriaServices.create(new ConvocatoriaRequest())).thenReturn(new Convocatoria());
		
		String json = mapper.writeValueAsString(new ConvocatoriaRequest());
		
		MvcResult response = mockMvc.perform(post("/convocatorias").contentType("application/json")
				.content(json)
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = mapper.writeValueAsString(new Convocatoria());
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testCreateNoAdmin() throws Exception{
		
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
		String json = mapper.writeValueAsString(new ConvocatoriaRequest());
		
		mockMvc.perform(post("/convocatorias").contentType("application/json")
				.content(json)
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
