package com.viewnext.login.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.MySQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.core.security.UsuarioDetails;
import com.viewnext.core.security.UsuarioDetailsService;
import com.viewnext.core.security.UtilsJWT;
import com.viewnext.core.security.UtilsOTP;
import com.viewnext.login.business.services.LoginServices;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UsuarioControllerLoginTest{
	
	 @LocalServerPort
	 private Integer port;
	
	@Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper mapper;
    
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private LoginServices loginServices;
    
    @MockitoBean
    private UsuarioRepository usuarioRepository;
    
    @MockitoBean
	private UtilsOTP utilsOTP;
    
    @MockitoBean
    private UtilsJWT utilsJWT;
    
    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;
    
    @MockitoBean
    private GoogleAuthenticator gAuth;
    
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    
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
    void executeSqlInContainer() throws IOException, InterruptedException {
        mysqlContainer.start();

        // Execute SQL statement with the database name
        String[] command = {"mysql", "-uuser", "-ppassword", "gestionformacion", "-e", "SHOW TABLES;"};
        Container.ExecResult result = mysqlContainer.execInContainer(command);

        // Print the output
        System.out.println(result.getStdout());
        System.out.println(result.getStderr());

        mysqlContainer.stop();

    }

    @Test
    void testLoginOk() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String otpCode = "123456";
        
        Usuario user = new Usuario();
        user.setEmail(email);
        user.setPassword(password);
        user.setSecreto("910121");

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        UsuarioDetails usuarioDetails = new UsuarioDetails(user);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(utilsJWT.generarJwt(authentication)).thenReturn("mockJwtToken");
        when(gAuth.authorize(any(String.class), any(Integer.class), any(Long.class))).thenReturn(true);
        when(utilsOTP.verifyCode(any(String.class), any(Integer.class))).thenReturn(true);
        when(usuarioDetailsService.loadUserByUsername(email)).thenReturn(usuarioDetails);

        mockMvc.perform(get("/autentificacion/login")
                .param("email", email)
                .param("password", password)
                .param("otpCode", otpCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockJwtToken"));
    }
    
	 @Test
	 void testLoginInvalidOtp() throws Exception {
		 String email = "test@example.com";
	        String password = "password";
	        String otpCode = "123456";
	        
	        Usuario user = new Usuario();
	        user.setEmail(email);
	        user.setPassword(password);
	        user.setSecreto("910121");

	        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
	        UsuarioDetails usuarioDetails = new UsuarioDetails(user);

	        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
	        when(usuarioRepository.findByEmail(email)).thenReturn(user);
	        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
	        when(utilsJWT.generarJwt(authentication)).thenReturn("mockJwtToken");
	        when(gAuth.authorize(any(String.class), any(Integer.class), any(Long.class))).thenReturn(false);
	        when(utilsOTP.verifyCode(any(String.class), any(Integer.class))).thenReturn(false);
	        when(usuarioDetailsService.loadUserByUsername(email)).thenReturn(usuarioDetails);

	        mockMvc.perform(get("/autentificacion/login")
	                .param("email", email)
	                .param("password", password)
	                .param("otpCode", otpCode))
	                .andExpect(status().isUnauthorized());
	     
	 }
}
