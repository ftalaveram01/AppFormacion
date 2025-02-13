package com.viewnext.login.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.viewnext.login.business.model.Usuario;
import com.viewnext.login.business.services.LoginServices;

@WebMvcTest(LoginController.class)
public class UsuarioControllerLoginTest extends AbstractControllerTest{

    @MockitoBean
    private LoginServices loginServices;

    private Usuario user;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup(){
        initObject();
        MockitoAnnotations.initMocks(this);
    }

	 @Test
	 void testLoginTrue() throws Exception {
	     
	     when(loginServices.login(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
	     
	     MvcResult mvcResult = mockMvc.perform(get("/autentificacion/login")
	             .param("email", user.getEmail())
	             .param("password", user.getPassword()))
	             .andExpect(status().isOk())
	             .andReturn();
	     	  
	     assertEquals(200, mvcResult.getResponse().getStatus());
	 }
    
	 @Test
	 void testLoginFalse() throws Exception {
		 when(loginServices.login("error@gmail.com", "123456")).thenReturn(Optional.empty());
		 
	     mockMvc.perform(get("/autentificacion/login")
	            .param("email", "error@email.com")
	            .param("password", "wrongpassword"))
	            .andExpect(status().isBadRequest());
	     
	 }

	    
	 //************************************
	 //
	 // Private method
	 //
	 //************************************

	 private void initObject() {
		 user = new Usuario();
		 user.setId(1L);
	     user.setEmail("prueba@gmail.com");
	     user.setPassword("1234");
	 }
	
}
