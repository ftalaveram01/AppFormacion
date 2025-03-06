package com.viewnext.usuario.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.usuario.integration.repositories.RolRepository;
import com.viewnext.usuario.integration.repositories.UsuarioRepository;

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
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
         
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
        
        Long id = usuarioServicesImpl.create(usuario, 1L);
        assertEquals(2L, id);
        
        
    }
    
    @Test
    void testDarAltaUserExistente() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
    	
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> 
                usuarioServicesImpl.create(usuario, 1L));
    }
    
    @Test
    void testDarAltaUserNotNull() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        
       assertThrows(IllegalStateException.class,() ->  
                       usuarioServicesImpl.create(usuario, 1L));
        
    }
    
    @Test
    void testDarAltaUserRolNoExistente() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        Rol rolv2 = new Rol();
        rolv2.setNombreRol(RolEnum.ADMIN);
        usuario.setRol(rolv2);
        
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(rolRepository.findByNombreRol(rolv2.getNombreRol())).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
                    usuarioServicesImpl.create(usuario, 1L);       
        });
        
    }
    
    @Test
    void testDarAltaUserRolSinId() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        Rol rolv2 = new Rol();
        rolv2.setNombreRol(RolEnum.ADMIN);
        usuario.setRol(rolv2);
        
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(rolRepository.findByNombreRol(rolv2.getNombreRol())).thenReturn(rolv2);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        
        when(usuarioRepository.save(usuario)).thenReturn(usuario2);
        Long id = usuarioServicesImpl.create(usuario, 1L);

        
        
    }
    
    @Test
    void testDeleteNoExisteUser() {
        when(usuarioRepository.existsById(any(Long.class))).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(1L, 1L));	
    }
    
    @Test
    void testDelete() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setHabilitado(true);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        
        usuarioServicesImpl.delete(2L, 1L);
        
        usuario.setHabilitado(false);
        
        verify(usuarioRepository, times(1)).save(usuario);
    }
    
    @Test
    void testDeleteDeshabilitado() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setHabilitado(false);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(2L, 1L));
    }
    
    @Test
    void testDeleteIdNoExistente() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        
        when(usuarioRepository.existsById(2L)).thenReturn(false);
        
        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(2L, 1L));
    }
    
    @Test
    void testUpdate() {
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario user = new Usuario();
        user.setId(2L);
        user.setRol(rol);
        
        when(usuarioRepository.existsById(2L)).thenReturn(true);

        usuarioServicesImpl.update(user, 1L);

        verify(usuarioRepository, times(1)).save(user);
        
    }
    
    @Test
    void testUpdateNoExiste() {
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario user = new Usuario();
        user.setId(2L);
        user.setRol(rol);
        
        when(usuarioRepository.existsById(2L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.update(user, 1L));
        
    }
    
    @Test
    public void testRead() {
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setRol(rol);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioServicesImpl.read(2L, 1L);

        verify(usuarioRepository, times(1)).findById(1L);
        assertEquals(usuario, resultado);
        
    }
    
    @Test
    public void testReadNoExistente() {
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));

        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setRol(rol);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.read(2L,  1L));
    }
    
    @Test
    void testGetAll() {
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	admin.setId(1L);
    	when(usuarioRepository.existsById(1L)).thenReturn(true);
    	when(usuarioRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setRol(rol);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        
        
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        
        List<Usuario> users = usuarioServicesImpl.getAll(1L);
        
        assertEquals(Arrays.asList(usuario1, usuario2), users);
        
    }
}
