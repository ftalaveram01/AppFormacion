package com.viewnext.usuario.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolAuthority;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.core.security.UsuarioDetails;
import com.viewnext.core.security.UsuarioDetailsService;
import com.viewnext.core.security.UtilsJWT;
import com.viewnext.core.security.UtilsOTP;
import com.viewnext.usuario.business.services.UsuarioServices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UsuarioControllerTest {
	
	 @LocalServerPort
	 private Integer port;
	
	@Autowired
	private MockMvc mockMvc;

    @MockitoBean
    private UsuarioServices usuarioServices;
    
    @MockitoBean
    private UtilsOTP utilsOTP;

    private UsuarioController usuarioController;
    
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

    private ObjectMapper objectMapper = new ObjectMapper();

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
      usuarioController = new UsuarioController(usuarioServices, utilsOTP);
    }
	
	@DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
      registry.add("spring.datasource.username", mysqlContainer::getUsername);
      registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
	
    @Test
    void testCreate() throws Exception {
    	
    	this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		 
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);
        when(usuarioServices.create(any(Usuario.class))).thenReturn(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .header("Authorization", jwtToken))
                .andExpect(status().isCreated())
                .andReturn();

        verify(usuarioServices, times(1)).create(any(Usuario.class));
        assertEquals("http://localhost/usuarios/1", result.getResponse().getHeader("Location"));
    }
    
    @Test
    void testCreateNoAdmin() throws Exception {
    	
    	this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		 
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void testDelete() throws Exception {
    	
    	this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();

        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/borrar/1")
        		.header("Authorization", jwtToken))
                .andExpect(status().isNoContent());

        verify(usuarioServices, times(1)).delete(1L);
    }
    
    @Test
    void testDeleteNoAdmin() throws Exception {
    	
    	this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
		
        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/borrar/1")
        		.header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdate() throws Exception {
    	
    	this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
    	
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);

        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .header("Authorization", jwtToken))
                .andExpect(status().isNoContent());

        verify(usuarioServices, times(1)).update(any(Usuario.class));
    }
    
    @Test
    void testUpdateNoAdmin() throws Exception {
    	
    	this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();
    	
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);

        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario))
                .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAll() throws Exception {
    	
    	Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);
    	
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
        when(usuarioServices.getAll()).thenReturn(Arrays.asList(usuario));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
        		.header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioServices, times(1)).getAll();
        assertEquals(objectMapper.writeValueAsString(Arrays.asList(usuario)), response.getResponse().getContentAsString());
    }
    
    @Test
    void testGetAllNoAdmin() throws Exception {
    	
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
        		.header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void testRead() throws Exception {
    	
        this.mockAdmin();
		String jwtToken = "Bearer " + this.generateJwtAdmin();
    	
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);
        when(usuarioServices.read(any(Long.class))).thenReturn(usuario);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1")
        		.header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioServices, times(1)).read(1L);
        assertEquals(objectMapper.writeValueAsString(usuario), result.getResponse().getContentAsString());
    }
    

    @Test
    void testReadNoAdmin() throws Exception {
    	
        this.mockAlumno();
		String jwtToken = "Bearer " + this.generateJwtAlumno();

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1")
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
