package com.viewnext.rol.presentation.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.rol.business.services.RolServices;

@WebMvcTest(RolController.class)
public class RolControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private RolServices rolServices;
	
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
    	
		MvcResult response = mockMvc.perform(get("/roles").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		String expected = mapper.writeValueAsString(Arrays.asList(rol, rol2));
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testCreate() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	String json = mapper.writeValueAsString(rol);
    	
    	when(rolServices.create(rol, 0L)).thenReturn(rol);
    	
		MvcResult response = mockMvc.perform(post("/roles").contentType("application/json")
				.param("idAdmin", "0")
				.content(json))
				.andExpect(status().isOk())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		
		assertEquals(json, actual);
	}
	
	@Test
	void testCreateError() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	String json = mapper.writeValueAsString(rol);
    	
    	when(rolServices.create(rol, 0L)).thenThrow(new IllegalStateException("Excepcion de create"));
    	
		MvcResult response = mockMvc.perform(post("/roles").contentType("application/json")
				.param("idAdmin", "0")
				.content(json))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		
		assertEquals("Excepcion de create", actual);
	}
	
	@Test
	void testRead() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	when(rolServices.read(5L)).thenReturn(rol);
    	
    	MvcResult response = mockMvc.perform(get("/roles/5").content("application/json"))
    			.andExpect(status().isOk())
    			.andReturn();
    	
    	String expected = mapper.writeValueAsString(rol);
    	String actual = response.getResponse().getContentAsString();
    	
    	assertEquals(expected, actual);
	}
	
	@Test
	void testReadError() throws Exception{
		when(rolServices.read(5L)).thenThrow(new IllegalStateException("Excepcion de read"));
		
		MvcResult response = mockMvc.perform(get("/roles/5").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		
		assertEquals("Excepcion de read", actual);
	}
	
	@Test
	void testUpdate() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
		when(rolServices.update(rol.getDescripcion(), 5L, 0L)).thenReturn(rol);
		
		String json = mapper.writeValueAsString(rol);
		
		MvcResult response = mockMvc.perform(put("/roles/5").contentType("text/plain")
				.param("idAdmin", "0")
				.content("Prueba"))
				.andExpect(status().isOk())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		
		assertEquals(json, actual);
	}
	
	@Test
	void testUpdateError() throws Exception{
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
		when(rolServices.update(rol.getDescripcion(), 5L, 0L)).thenThrow(new IllegalStateException("Excepcion de update"));
		
		String json = mapper.writeValueAsString(rol);
		
		MvcResult response = mockMvc.perform(put("/roles/5").contentType("text/plain")
				.param("idAdmin", "0")
				.content("Prueba"))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String actual = response.getResponse().getContentAsString();
		
		assertEquals("Excepcion de update", actual);
	}
	
	@Test
	void testDelete() throws Exception{
		
		mockMvc.perform(delete("/roles/5").contentType("application/json").param("idAdmin", "0"))
			.andExpect(status().isNoContent())
			.andReturn();
		
		verify(rolServices, times(1)).delete(5L, 0L);
	}
	
	@Test
	void testDeleteError() throws Exception{
		
		doThrow(new IllegalStateException("Excepcion de delete")).when(rolServices).delete(5L, 0L);;
		
		mockMvc.perform(delete("/roles/5").contentType("application/json").param("idAdmin", "0"))
			.andExpectAll(status().isBadRequest())
			.andReturn();
	}

}
