package com.viewnext.course.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolAuthority;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.core.security.UsuarioDetails;
import com.viewnext.core.security.UsuarioDetailsService;
import com.viewnext.core.security.UtilsJWT;
import com.viewnext.course.business.services.CourseServices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CourseControllerTest{
	
	private Long jwtExpirationMs = 3600000L;
	private String jwtSecret = "TkpwTVpaMTFpVTBHWktjV0Y5AL9JdFJra0ZxPVRlZTN2ZkJjR0hrYjAMRs1=";
	
	@LocalServerPort
	 private Integer port;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private CourseServices courseServices;
	
	@MockitoBean
	private UsuarioRepository usuarioRepository;
	
	@MockitoBean
	private UsuarioDetailsService usuarioDetailsService;
	
	@MockitoBean
	private UtilsJWT utilsJWT;
	
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0")
    		.withDatabaseName("gestionformacion")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("schema.sql");
	
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
		Course course = new Course();
		course.setId(2L);
		Course coursev2 = new Course();
		coursev2.setId(3L);
		
		this.mockAlumno();
		
		when(courseServices.getAll()).thenReturn(Arrays.asList(course, coursev2));
		
		MvcResult response = mockMvc.perform(get("/courses").with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))).contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(course, coursev2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testRead() throws Exception{
		Course course = new Course();
		course.setId(2L);
		
		when(courseServices.read(2L)).thenReturn(Optional.of(course));
		
		MvcResult response = mockMvc.perform(get("/courses/2").with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))).contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(course);
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testReadNotFound() throws Exception{
		
		when(courseServices.read(any(Long.class))).thenReturn(Optional.empty());
		
		MvcResult response = mockMvc.perform(get("/courses/2").with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))).contentType("application/json"))
				.andExpect(status().isNotFound())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		assertEquals("", responseBody);
	}
	
	@Test
	void testCreate() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		when(courseServices.create(any(Course.class))).thenReturn(2L);
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(post("/courses")
				.contentType("application/json")
				.content("{\"nombre\":\"ROLE_USER\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = response.getResponse().getHeader("Location");
		String expected = "http://localhost/courses/2";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testCreateExistente() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		when(courseServices.create(any(Course.class))).thenThrow(new IllegalStateException("Excepcion de create"));
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(post("/courses")
				.contentType("application/json")
				.content("{\"nombre\":\"ROLE_USER\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Excepcion de create";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testUpdate() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(put("/courses/2")
				.contentType("application/json")
				.content("{\"nombre\":\"ROLE_USER\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		verify(courseServices, times(1)).update(any(Course.class), eq(2L));
		assertEquals("", responseBody);
	}
	
	@Test
	void testUpdateNoExistente() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		doThrow(new IllegalStateException("Error de update")).when(courseServices).update(any(Course.class), eq(2L));
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(put("/courses/2")
				.contentType("application/json")
				.content("{\"nombre\":\"ROLE_USER\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isBadRequest())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error de update";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testDelete() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		mockMvc.perform(delete("/courses/2")
				.contentType("application/json")
				.content("{\"nombre\":\"ROLE_USER\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(courseServices,times(1)).delete(2L);
	}
	
	@Test
	void testDeleteNoExistente() throws Exception{
		
		this.mockAdmin();
		
		String jwtToken = "Bearer " + this.generateJwtAdmin();
		
		doThrow(new IllegalStateException("Error de delete")).when(courseServices).delete(2L);
		
		MvcResult response = mockMvc.perform(delete("/courses/2")
				.contentType("application/json")
				.content("{\\\"nombre\\\":\\\"ROLE_USER\\\"}")
				.header("Authorization", jwtToken))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error de delete";
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testAlumnosEnCurso() throws Exception{
		Course curso = new Course();
		curso.setId(1L);
		
		Rol rolAlumno = new Rol();
		rolAlumno.setId(1L);
		
		Usuario alumno1 = new Usuario();
		alumno1.setId(1L);
		alumno1.setEmail("alumno1@gmail.com");
		alumno1.setPassword("1234");
		alumno1.setRol(rolAlumno);
		
		Usuario alumno2 = new Usuario();
		alumno2.setId(2L);
		alumno2.setEmail("alumno2@gmail.com");
		alumno2.setPassword("1234");
		alumno2.setRol(rolAlumno);
		
		Usuario alumno3 = new Usuario();
		alumno3.setId(3L);
		alumno3.setEmail("alumno3@gmail.com");
		alumno3.setPassword("1234");
		alumno3.setRol(rolAlumno);
		
		List<Usuario> listaAlumnos = new ArrayList<>();
		listaAlumnos.add(alumno1);
		listaAlumnos.add(alumno2);
		listaAlumnos.add(alumno3);
		
		curso.setUsuarios(listaAlumnos);
		
		when(courseServices.read(2L)).thenReturn(Optional.of(curso));
		
		MvcResult response = mockMvc.perform(get("/courses/2").with(jwt().jwt(jwt -> jwt.claim("roles", "ADMIN"))).contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
	    String expected = mapper.writeValueAsString(listaAlumnos);
	    
	    String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
	    Course courseResponse = mapper.readValue(responseBody, Course.class);
	    String actual = mapper.writeValueAsString(courseResponse.getUsuarios());

	    assertEquals(expected, actual);
	}
	
	@Test
	void testAlumnoEnCursoNoExistente() throws Exception {
		doThrow(new IllegalStateException("Error curso no existente")).when(courseServices).read(2L);

		MvcResult response = mockMvc.perform(get("/courses/2").with(jwt().jwt(jwt -> jwt.claim("roles", "ADMIN"))).contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();

		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error curso no existente";

		assertEquals(expected, responseBody);
	}
	@Test

	void testDeleteUsuarioExistenteDeCurso() throws Exception{
		
		int idUsuario = 1;
		int idCurso = 1;
		
		mockMvc.perform(delete("/courses")
				.with(jwt().jwt(jwt -> jwt.claim("roles", "ADMIN")))
				.param("idUsuario", String.valueOf(idUsuario))
				.param("idCurso", String.valueOf(idCurso))
				.contentType("aplication/json"))
			.andExpect(status().isNoContent())
			.andReturn();
		
		verify(courseServices,times(1)).deleteUsuario(1L, 1L);
	}
	
	@Test
	void testDeleteUsuarioNoExistenteDeCurso() throws Exception{
		doThrow(new IllegalStateException("Error de delete")).when(courseServices).deleteUsuario(2L, 2L);
		
		int idUsuario = 2;
		int idCurso = 2;
		
		MvcResult response = mockMvc.perform(delete("/courses")
				.with(jwt().jwt(jwt -> jwt.claim("roles", "ADMIN")))
				.param("idUsuario", String.valueOf(idUsuario))
				.param("idCurso", String.valueOf(idCurso))
				.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error de delete";
		assertEquals(expected, responseBody);
	}

	@Test
	void testInscribirAlumno() throws Exception{
		
		MvcResult response = mockMvc.perform(put("/courses").with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))).param("idUsuario", "5").param("idCurso", "3").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		verify(courseServices, times(1)).inscribir(5L, 3L);
		assertEquals("", responseBody);
		
	}
	
	@Test
	void testInscribirAlumnoCursoNoExistente() throws Exception{
		
		doThrow(new IllegalStateException("Usuario o curso no existente")).when(courseServices).inscribir(5L, 3L);
		
		MvcResult response = mockMvc.perform(put("/courses").with(jwt().jwt(jwt -> jwt.claim("roles", "USER"))).param("idUsuario", "5").param("idCurso", "3").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertEquals("Usuario o curso no existente", responseBody);
		
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
