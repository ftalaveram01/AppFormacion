package com.viewnext.course.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Course;
import com.viewnext.course.business.services.CourseServices;

@WebMvcTest(CursoController.class)
class CourseControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private CourseServices courseServices;
	
	@Test
	void testGetAll() throws Exception{
		Course course = new Course();
		course.setId(2L);
		Course coursev2 = new Course();
		coursev2.setId(3L);
		
		when(courseServices.getAll()).thenReturn(Arrays.asList(course, coursev2));
		
		MvcResult response = mockMvc.perform(get("/courses").contentType("application/json"))
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
		
		MvcResult response = mockMvc.perform(get("/courses/2").contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String json = mapper.writeValueAsString(course);
		
		assertEquals(json, responseBody);
	}
	
	@Test
	void testReadNotFound() throws Exception{
		
		when(courseServices.read(any(Long.class))).thenReturn(Optional.empty());
		
		MvcResult response = mockMvc.perform(get("/courses/2").contentType("application/json"))
				.andExpect(status().isNotFound())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		assertEquals("", responseBody);
	}
	
	@Test
	void testCreate() throws Exception{
		when(courseServices.create(any(Course.class), eq(5L))).thenReturn(2L);
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(post("/courses").param("idAdmin", "5").contentType("application/json").content(json))
				.andExpect(status().isCreated())
				.andReturn();
		
		String responseBody = response.getResponse().getHeader("Location");
		String expected = "http://localhost/courses/2";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testCreateExistente() throws Exception{
		when(courseServices.create(any(Course.class), eq(5L))).thenThrow(new IllegalStateException("Excepcion de create"));
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(post("/courses").param("idAdmin", "5").contentType("application/json").content(json))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Excepcion de create";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testUpdate() throws Exception{
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(put("/courses/2").param("idAdmin", "5").contentType("application/json").content(json))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		verify(courseServices, times(1)).update(any(Course.class), eq(2L), eq(5L));
		assertEquals("", responseBody);
	}
	
	@Test
	void testUpdateNoExistente() throws Exception{
		
		doThrow(new IllegalStateException("Error de update")).when(courseServices).update(any(Course.class), eq(2L), eq(5L));
		
		String json = mapper.writeValueAsString(new Course());
		
		MvcResult response = mockMvc.perform(put("/courses/2").param("idAdmin", "5").contentType("application/json").content(json))
				.andExpect(status().isBadRequest())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error de update";
		
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testDelete() throws Exception{
		
		mockMvc.perform(delete("/courses/2").param("idAdmin", "5"))
				.andExpect(status().isNoContent())
				.andReturn();
		
		verify(courseServices,times(1)).delete(2L, 5L);
	}
	
	@Test
	void testDeleteNoExistente() throws Exception{
		doThrow(new IllegalStateException("Error de delete")).when(courseServices).delete(2L, 5L);
		
		MvcResult response = mockMvc.perform(delete("/courses/2").param("idAdmin", "5").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error de delete";
		assertEquals(expected, responseBody);
	}
	
	@Test
	void testInscribirAlumno() throws Exception{
		
		MvcResult response = mockMvc.perform(put("/courses").param("idUsuario", "5").param("idCurso", "3").contentType("application/json"))
				.andExpect(status().isCreated())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		verify(courseServices, times(1)).inscribir(5L, 3L);
		assertEquals("", responseBody);
		
	}
	
	@Test
	void testInscribirAlumnoCursoNoExistente() throws Exception{
		
		doThrow(new IllegalStateException("Usuario o curso no existente")).when(courseServices).inscribir(5L, 3L);
		
		MvcResult response = mockMvc.perform(put("/courses").param("idUsuario", "5").param("idCurso", "3").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertEquals("Usuario o curso no existente", responseBody);
		
	}
}
