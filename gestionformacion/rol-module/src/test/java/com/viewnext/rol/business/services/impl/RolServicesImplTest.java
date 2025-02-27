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
import com.viewnext.rol.integration.repositories.RolRepository;

@ExtendWith(MockitoExtension.class)
public class RolServicesImplTest {
	
	@Mock
	private RolRepository rolRepository;
	
    @InjectMocks
    private RolServicesImpl rolServices;
    
    @Test
    void testCreate() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol(RolEnum.ADMIN);
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	when(rolRepository.save(rol)).thenReturn(rol);
    	
    	Rol guardado = rolServices.create(rol);
    	
    	assertEquals(rol, guardado);
    }
    
    @Test
    void testCreateIdNull() {
    	Rol rol = new Rol();
    	rol.setId(null);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.create(rol)); 
    	
    }
    
    @Test
    void testCreateIdExistente() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.create(rol)); 
    	
    }
    
    @Test
    void testDelete() {
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	
    	rolServices.delete(5L);
    	verify(rolRepository,times(1)).deleteById(5L);
    }
    
    @Test
    void testDeleteNoExiste() {
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.delete(5L));
    	
    }
    
    @Test
    void testUpdate() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol(RolEnum.ADMIN);
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	when(rolRepository.save(rol)).thenReturn(rol);
    	
    	Rol creado = rolServices.update(rol, 5L);
    	assertEquals(rol, creado);
    }
    
    @Test
    void testUpdateIdNull() {
    	Rol rol = new Rol();
    	rol.setId(null);
    	rol.setNombreRol(RolEnum.ADMIN);
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(true);
    	when(rolRepository.save(rol)).thenReturn(rol);
    	
    	Rol creado = rolServices.update(rol, 5L);
    	assertEquals(5L, creado.getId());
    	rol.setId(5L);
    	assertEquals(rol, creado);
    }
    
    @Test
    void testUpdateIdDistinto() {
    	Rol rol = new Rol();
    	rol.setId(4L);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.update(rol, 5L));
    }
    
    @Test
    void testUpdateNoExistente() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol(RolEnum.ADMIN);
    	rol.setDescripcion("Prueba");
    	
    	when(rolRepository.existsById(5L)).thenReturn(false);
    	
    	assertThrows(IllegalStateException.class, () -> rolServices.update(rol, 5L));
    }
    
    @Test
    void testGetAll() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol(RolEnum.ADMIN);
    	rol.setDescripcion("Prueba");
    	
    	Rol rol2 = new Rol();
    	rol2.setId(5L);
    	rol2.setNombreRol(RolEnum.ALUMNO);
    	rol2.setDescripcion("Prueba2");
    	
    	when(rolRepository.findAll()).thenReturn(Arrays.asList(rol, rol2));
    	
    	List<Rol> roles = rolServices.getAll();
    	
    	assertTrue(roles.containsAll(Arrays.asList(rol, rol2)));
    }
    
    @Test
    void testRead() {
    	Rol rol = new Rol();
    	rol.setId(5L);
    	rol.setNombreRol(RolEnum.ADMIN);
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
