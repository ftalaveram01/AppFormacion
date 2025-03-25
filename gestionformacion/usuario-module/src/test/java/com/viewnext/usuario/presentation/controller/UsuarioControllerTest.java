package com.viewnext.usuario.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.business.services.UsuarioServices;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioServices usuarioServices;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    void testCreate() throws Exception {
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);
        when(usuarioServices.create(any(Usuario.class))).thenReturn(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(usuarioServices, times(1)).create(any(Usuario.class));
        assertEquals("http://localhost/usuarios/1", result.getResponse().getHeader("Location"));
    }

    @Test
    void testDelete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/borrar/1"))
                .andExpect(status().isNoContent());

        verify(usuarioServices, times(1)).delete(1L);
    }

    @Test
    void testUpdate() throws Exception {
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);

        mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNoContent());

        verify(usuarioServices, times(1)).update(any(Usuario.class));
    }

    @Test
    void testGetAll() throws Exception {
        when(usuarioServices.getAll()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isOk());

        verify(usuarioServices, times(1)).getAll();
    }

    @Test
    void testRead() throws Exception {
        Rol rol= new Rol();
        rol.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail("prueba@gmail.com");
        usuario.setPassword("1234");
        usuario.setRol(rol);
        when(usuarioServices.read(any(Long.class))).thenReturn(usuario);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioServices, times(1)).read(1L);
        assertEquals(objectMapper.writeValueAsString(usuario), result.getResponse().getContentAsString());
    }
}
