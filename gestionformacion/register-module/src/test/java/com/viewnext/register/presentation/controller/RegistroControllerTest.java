package com.viewnext.register.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.register.business.services.RegistroService;


@WebMvcTest(RegistroController.class)
class RegistroControllerTest extends AbstractControllerTest{
	
	@Autowired
	private MockMvc mockMvc;

    @MockitoBean
    private RegistroService registroService;
    
	@Autowired
	private ObjectMapper mapper;

    
    @Test
    void testRegistrar() throws Exception {
    	
    	Usuario usuario = new Usuario();
    	usuario.setRol(new Rol());

        when(registroService.register(any(Usuario.class))).thenReturn(500L);
        
        String json = mapper.writeValueAsString(usuario);

        MvcResult result = mockMvc.perform(post("/autentificacion/registrar").contentType("application/json").content(json))
				.andExpect(status().isCreated())
				.andReturn();

		String responseBody = result.getResponse().getHeader("Location");
		String expected = "http://localhost/usuarios/500";
		
		assertEquals(expected, responseBody);
    }
    
    @Test
    void testRegistrarExistente() throws Exception {
    	
    	Usuario usuario = new Usuario();
    	usuario.setRol(new Rol());

        when(registroService.register(any(Usuario.class))).thenThrow(new IllegalStateException("Excepcion de registro"));
        
        String json = mapper.writeValueAsString(usuario);

        MvcResult result = mockMvc.perform(post("/autentificacion/registrar").contentType("application/json").content(json))
				.andExpect(status().isBadRequest())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Excepcion de registro";
		
		assertEquals(expected, responseBody);
    }

}
