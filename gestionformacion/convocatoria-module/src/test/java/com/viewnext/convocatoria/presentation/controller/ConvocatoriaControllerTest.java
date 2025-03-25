package com.viewnext.convocatoria.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.Arrays;

import com.viewnext.convocatoria.model.ConvocatoriaRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;

@WebMvcTest(ConvocatoriaController.class)
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class ConvocatoriaControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private ConvocatoriaServices convocatoriaServices;
	
	@Test
	void testGetAll() throws Exception{
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getAll()).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testGetActivas() throws Exception{
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getActivas()).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias/activas").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testGetFromUsuario() throws Exception{
		Convocatoria conv = new Convocatoria();
		conv.setId(2L);
		Convocatoria conv2 = new Convocatoria();
		conv2.setId(3L);
		
		when(convocatoriaServices.getFromUsuario(2L)).thenReturn(Arrays.asList(conv, conv2));
		
		MvcResult response = mockMvc.perform(get("/convocatorias/usuario/2").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(Arrays.asList(conv, conv2));
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testInscribirExcepcion() throws Exception{
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .inscribirUsuario(5L, 2L);
		
		mockMvc.perform(put("/convocatorias/2/inscribir").contentType("application/json")
				.param("idUsuario", "5"))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testInscribir() throws Exception{
		
		mockMvc.perform(put("/convocatorias/2/inscribir").contentType("application/json")
				.param("idUsuario", "5"))
				.andExpect(status().isOk())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).inscribirUsuario(5L, 2L);
	}
	
	@Test
	void testEnviarCertificadoExcepcion() throws Exception{
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .generarCertificado(2L, 5L);
		
		mockMvc.perform(post("/convocatorias/2/usuarios/certificado").contentType("application/json")
				.param("idUsuario", "5"))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testEnviarCertificado() throws Exception{
		
		mockMvc.perform(post("/convocatorias/2/usuarios/certificado").contentType("application/json")
				.param("idUsuario", "5"))
				.andExpect(status().isOk())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).generarCertificado(2L, 5L);
	}
	
	@Test
	void testDeleteExcepcion() throws Exception{
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .delete(2L);
		
		mockMvc.perform(delete("/convocatorias/2").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testDelete() throws Exception{
		
		mockMvc.perform(delete("/convocatorias/2").contentType("application/json"))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).delete(2L);
	}
	
	@Test
	void testUpdateExcepcion() throws Exception{
	    doThrow(new IllegalStateException("Error al inscribir usuario"))
	    .when(convocatoriaServices)
	    .update(2L, new UpdateRequest());
	    
	    String json = mapper.writeValueAsString(new UpdateRequest());
		
		mockMvc.perform(put("/convocatorias/2").contentType("application/json")
				.content(json))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testUpdate() throws Exception{
		
	    String json = mapper.writeValueAsString(new UpdateRequest());
		
		mockMvc.perform(put("/convocatorias/2").contentType("application/json")
				.content(json))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(convocatoriaServices, times(1)).update(2L, new UpdateRequest());
	}
	
	@Test
	void testCreateExcepcion() throws Exception{
		when(convocatoriaServices.create(new ConvocatoriaRequest())).thenThrow(new IllegalStateException("Excepcion de create"));
		
		String json = mapper.writeValueAsString(new ConvocatoriaRequest());
		
		MvcResult response = mockMvc.perform(post("/convocatorias").contentType("application/json").content(json))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Excepcion de create";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testCreate() throws Exception{
		when(convocatoriaServices.create(new ConvocatoriaRequest())).thenReturn(new Convocatoria());
		
		String json = mapper.writeValueAsString(new ConvocatoriaRequest());
		
		MvcResult response = mockMvc.perform(post("/convocatorias").contentType("application/json").content(json))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = mapper.writeValueAsString(new Convocatoria());
		
		assertEquals(expected, responseBody);
	}

}
