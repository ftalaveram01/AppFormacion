package com.viewnext.rol.business.services.impl;

import static org.mockito.Mockito.times;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.core.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class RolServicesImplTest {
	
	@Mock
	private RolRepository rolRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
    @InjectMocks
    private RolServicesImpl rolServices;
    
    @Test
    void testCreate() {
    	Usuario usuario = new Usuario();
    	Rol rolv2 = new Rol();
    	rolv2.setNombreRol("ADMIN");
    	usuario.setRol(rolv2);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	when(rolRepository.save(rol)).thenReturn(rol);
    	
    	Rol guardado = rolServices.create(rol, 3L);
    	
    	assertEquals(rol, guardado);
    }
    
    @Test
    void testCreateIdNull() {
    	Usuario usuario = new Usuario();
    	Rol rolv2 = new Rol();
    	rolv2.setNombreRol("ADMIN");
    	usuario.setRol(rolv2);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	Rol rol = new Rol();
    	rol.setId(null);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.create(rol, 3L)); 
    	
    }
    
    @Test
    void testCreateIdExistente() {
    	Usuario usuario = new Usuario();
    	Rol rolv2 = new Rol();
    	rolv2.setNombreRol("ADMIN");
    	usuario.setRol(rolv2);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	Rol rol = new Rol();
    	rol.setId(5L);
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.create(rol, 3L)); 
    	
    }
    
    @Test
    void testDelete() {
    	Usuario usuario = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
    	usuario.setRol(rol);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	
    	rolServices.delete(5L, 3L);
    	verify(rolRepository,times(1)).deleteById(5L);
    }
    
    @Test
    void testDeleteNoExiste() {
    	Usuario usuario = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
    	usuario.setRol(rol);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.delete(5L, 3L));
    	
    }
    
    @Test
    void testUpdate() {
    	Usuario usuario = new Usuario();
    	Rol rolv1 = new Rol();
    	rolv1.setNombreRol("ADMIN");
    	usuario.setRol(rolv1);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	when(rolRepository.findById(5L)).thenReturn(Optional.of(rol));
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	when(rolRepository.save(rol)).thenReturn(rol);
    	
    	Rol creado = rolServices.update(rol.getDescripcion(), 5L, 3L);
    	assertEquals(rol, creado);
    }
    
    @Test
    void testUpdateNoExistente() {
    	Usuario usuario = new Usuario();
    	Rol rolv2 = new Rol();
    	rolv2.setNombreRol("ADMIN");
    	usuario.setRol(rolv2);
    	usuario.setId(3L);
    	when(usuarioRepository.existsById(3L)).thenReturn(true);
    	when(usuarioRepository.isAdmin(3L)).thenReturn(true);
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.update(rol.getDescripcion(), 5L, 3L));
    }
    
    @Test
    void testGetAll() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	Rol rol2 = new Rol();
    	rol2.setId(5L);
    	rol2.setNombreRol("ADMIN");
    	rol2.setDescripcion("Prueba2");
    	
    	when(rolRepository.findAll()).thenReturn(Arrays.asList(rol, rol2));
    	
    	List<Rol> roles = rolServices.getAll();
    	
    	assertTrue(roles.containsAll(Arrays.asList(rol, rol2)));
    }
    
    @Test
    void testRead() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol("ADMIN");
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	when(rolRepository.findById(5L)).thenReturn(Optional.of(rol));
    	
    	Rol encontrado = rolServices.read(5L);
    	
    	assertEquals(rol, encontrado);
    }
    
    @Test
    void testReadNoExistente() {
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	assertThrows(IllegalStateException.class, () -> rolServices.read(5L));
    }

}
