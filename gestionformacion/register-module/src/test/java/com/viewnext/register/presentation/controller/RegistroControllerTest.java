package com.viewnext.register.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.security.UtilsOTP;
import com.viewnext.core.security.payloads.RegistroResponse;
import com.viewnext.register.business.services.RegistroService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RegistroControllerTest extends AbstractControllerTest{
	
	@Autowired
	private MockMvc mockMvc;

    @MockitoBean
    private RegistroService registroService;
    
    @MockitoBean
    private UtilsOTP utilsOTP;
    
    @MockitoBean
    private GoogleAuthenticatorKey authenticatorKey;
    
    @MockitoBean
    private GoogleAuthenticatorQRGenerator authenticatorQRGenerator;
    
    
    private RegistroController registroController;
    
	@Autowired
	private ObjectMapper mapper;

	@LocalServerPort
	private Integer port;
	
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
      registroController = new RegistroController(registroService, utilsOTP);
    }
	
	@DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
      registry.add("spring.datasource.username", mysqlContainer::getUsername);
      registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
    
	@Test
	void testRegistrar() throws Exception {
	    
	    Usuario usuario = new Usuario();
	    Rol rol = new Rol();
	    rol.setNombreRol("USER");
	    
	    usuario.setId(500L);
	    usuario.setEmail("ejemplo@gmail.com");
	    usuario.setPassword("1234");
	    usuario.setRol(rol);
	    usuario.setHabilitado(true);
	    
	    GoogleAuthenticatorKey key = mock(GoogleAuthenticatorKey.class);
	    when(key.getKey()).thenReturn("fakeKey");
	    String qr = "fakeQRCode";
	    
	    when(utilsOTP.generateKey()).thenReturn(key);
	    when(utilsOTP.generateQRCode(usuario.getEmail(), key)).thenReturn(qr);
	    when(registroService.register(usuario)).thenReturn(500L);
	    
	    String expectedJsonResponse = "{\"qr\":\"fakeQRCode\",\"secreto\":\"fakeKey\"}";

	    MvcResult result = mockMvc.perform(post("/autentificacion/registrar")
	            .contentType("application/json")
	            .content(expectedJsonResponse))
	            .andExpect(status().isOk())
	            .andReturn();

	}

    
	@Test
	void testRegistrarExistente() throws Exception {
		
	    Usuario usuario = new Usuario();
	    Rol rol = new Rol();
	    rol.setNombreRol("USER");
	    
	    usuario.setId(500L);
	    usuario.setEmail("ejemplo@gmail.com");
	    usuario.setPassword("1234");
	    usuario.setRol(rol);
	    usuario.setHabilitado(true);

	    GoogleAuthenticatorKey key = mock(GoogleAuthenticatorKey.class);
	    when(key.getKey()).thenReturn("fakeKey");

	    String qr = "fakeQRCode";

	    when(utilsOTP.generateKey()).thenReturn(key);
	    when(utilsOTP.generateQRCode(usuario.getEmail(), key)).thenReturn(qr);
	    
	    when(registroService.register(usuario)).thenThrow(new IllegalStateException("Ya existe un usuario con ese email."));

	    String expectedJsonResponse = "Ya existe un usuario con ese email.";

	    MvcResult result = mockMvc.perform(post("/autentificacion/registrar")
	            .contentType("application/json")
	            .content("{\"email\":\"ejemplo@gmail.com\",\"password\":\"1234\",\"rol\":{\"nombreRol\":\"USER\"}}"))
	            .andExpect(status().isOk())
	            .andReturn();

	}


}
