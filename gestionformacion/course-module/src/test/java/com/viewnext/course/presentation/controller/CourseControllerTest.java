package com.viewnext.course.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
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
		
		MvcResult response = mockMvc.perform(get("/courses/2/alumnos").contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
	    String json = mapper.writeValueAsString(listaAlumnos);

	    assertEquals(json, responseBody);
	}
	
	@Test
	void testAlumnoEnCursoNoExistente() throws Exception {
		doThrow(new IllegalStateException("Error curso no existente")).when(courseServices).read(2L);

		MvcResult response = mockMvc.perform(get("/courses/2/alumnos").contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andReturn();

		String responseBody = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
		String expected = "Error curso no existente";

		assertEquals(expected, responseBody);
	}

}
