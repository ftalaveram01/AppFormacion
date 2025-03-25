package com.viewnext.usuario.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.core.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServicesImpl usuarioServicesImpl;
    
    @Test
    void testDarAltaUser() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
         
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        Rol rolv2 = new Rol();
        rolv2.setId(0L);
        usuario.setRol(rolv2);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario2);
        
        Long id = usuarioServicesImpl.create(usuario);
        assertEquals(2L, id);
        
        
    }
    
    @Test
    void testDarAltaUserExistente() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
    	
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> 
                usuarioServicesImpl.create(usuario));
    }
    
    @Test
    void testDarAltaUserNotNull() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        
       assertThrows(IllegalStateException.class,() ->  
                       usuarioServicesImpl.create(usuario));
        
    }
    
    @Test
    void testDarAltaUserRolNoExistente() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");   
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        Rol rolv2 = new Rol();
        rolv2.setNombreRol("ADMIN");
        usuario.setRol(rolv2);
        
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(rolRepository.findByNombreRol(rolv2.getNombreRol())).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
                    usuarioServicesImpl.create(usuario);       
        });
        
    }
    
    @Test
    void testDarAltaUserRolSinId() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
        
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        Rol rolv2 = new Rol();
        rolv2.setNombreRol("ADMIN");
        usuario.setRol(rolv2);
        
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(rolRepository.findByNombreRol(rolv2.getNombreRol())).thenReturn(rolv2);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        
        when(usuarioRepository.save(usuario)).thenReturn(usuario2);
        
        Long id = usuarioServicesImpl.create(usuario);
        assertEquals(2L, id);

    }
    
    @Test
    void testDeleteNoExisteUser() {
        when(usuarioRepository.existsById(any(Long.class))).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(1L));	
    }
    
    @Test
    void testDelete() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
        
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setHabilitado(true);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        
        usuarioServicesImpl.delete(2L);
        
        usuario.setHabilitado(false);
        
        verify(usuarioRepository, times(1)).save(usuario);
    }
    
    @Test
    void testDeleteDeshabilitado() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");  
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setHabilitado(false);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(2L));
    }
    
    @Test
    void testDeleteIdNoExistente() {
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
        
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        
        when(usuarioRepository.existsById(2L)).thenReturn(false);
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(2L));
    }
    
    @Test
    void testUpdate() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
        
        Usuario user = new Usuario();
        user.setId(2L);
        user.setRol(rol);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);

        usuarioServicesImpl.update(user);

        verify(usuarioRepository, times(1)).save(user);
        
    }
    
    @Test
    void testUpdateNoExiste() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");      
        Usuario user = new Usuario();
        user.setId(2L);
        user.setRol(rol);
        
        when(usuarioRepository.existsById(2L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.update(user));
        
    }
    
    @Test
    void testRead() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setRol(rol);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioServicesImpl.read(2L);

        verify(usuarioRepository, times(1)).findById(2L);
        assertEquals(usuario, resultado);
        
    }
    
    @Test
    void testReadNoExistente() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setRol(rol);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.read(2L));
    }
    
    @Test
    void testGetAll() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol("ADMIN");
        
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setRol(rol);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        
        
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        
        List<Usuario> users = usuarioServicesImpl.getAll();
        
        assertEquals(Arrays.asList(usuario1, usuario2), users);
        
    }
    
    @Test
    void testDeshabilitar() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setEmail("hola@gmail.com");
        usuario1.setHabilitado(true);
        
        when(usuarioRepository.existsByEmail(usuario1.getEmail())).thenReturn(true);
        when(usuarioRepository.findByEmail(usuario1.getEmail())).thenReturn(usuario1);
        
        usuarioServicesImpl.deshabilitarUsuario(usuario1.getEmail());
        
        usuario1.setHabilitado(false);
        
        verify(usuarioRepository, times(1)).save(usuario1);
    }
    
    @Test
    void testDeshabilitarYaDeshabilitado() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setEmail("hola@gmail.com");
        usuario1.setHabilitado(false);
        
        when(usuarioRepository.existsByEmail(usuario1.getEmail())).thenReturn(true);
        when(usuarioRepository.findByEmail(usuario1.getEmail())).thenReturn(usuario1);
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.deshabilitarUsuario(usuario1.getEmail()));
    }
    
    @Test
    void testDeshabilitarNoExistente() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setEmail("hola@gmail.com");
        usuario1.setHabilitado(true);
        
        when(usuarioRepository.existsByEmail(usuario1.getEmail())).thenReturn(false);
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.deshabilitarUsuario(usuario1.getEmail()));
    }
}
