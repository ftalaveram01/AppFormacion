package com.viewnext.register.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.register.integration.repository.RolRepository;
import com.viewnext.register.integration.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class RegistroServiceImplTest {

	@Mock
    private UsuarioRepository usuarioRepository;
	
	@Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RegistroServiceImpl registroServiceImpl;

    @Test
    void testRegisterNuevoUsuario(){
    	
    	Usuario usuario = new Usuario();
    	usuario.setId(null);
    	usuario.setEmail("test@gmail.com");
    	Rol rol = new Rol();
    	rol.setId(0L);
    	usuario.setRol(rol);
    	
    	Usuario usuariov2 = new Usuario();
    	usuario.setId(2L);
    	usuario.setEmail("test@gmail.com");
    	usuario.setRol(rol);

        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuariov2);

        Long idRegistro = registroServiceImpl.register(usuario);

        assertEquals(usuariov2.getId(), idRegistro);
        verify(rolRepository, times(0)).findByNombreRol(any(RolEnum.class));

    }

    @Test
    void testRegisterUsuarioExistente(){
    	
    	Usuario usuario = new Usuario();
    	usuario.setId(null);
    	usuario.setEmail("test@gmail.com");

        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(true);

       assertThrows(IllegalStateException.class,() ->  registroServiceImpl.register(usuario));
    }

    @Test
    void testRegisterIdNotNull(){

    	Usuario usuario = new Usuario();
    	usuario.setId(2L);
        
       assertThrows(IllegalStateException.class,() ->  registroServiceImpl.register(usuario));
    }

    @Test
    void testSinIdRol(){

    	Usuario usuario = new Usuario();
    	usuario.setId(null);
    	usuario.setEmail("test@gmail.com");
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	usuario.setRol(rol);
    	
    	Usuario usuariov2 = new Usuario();
    	usuario.setId(2L);
    	usuario.setEmail("test@gmail.com");
    	usuario.setRol(rol);
    	
        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuariov2);
        when(rolRepository.findByNombreRol(any(RolEnum.class))).thenReturn(rol);

        Long idRegistro = registroServiceImpl.register(usuario);
        
        assertEquals(usuariov2.getId(), idRegistro);
        
    }
    
    @Test
    void testRolNoExistente(){

    	Usuario usuario = new Usuario();
    	usuario.setId(null);
    	usuario.setEmail("test@gmail.com");
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	usuario.setRol(rol);
    	
        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(rolRepository.findByNombreRol(any(RolEnum.class))).thenReturn(null);

        assertThrows(IllegalStateException.class,() ->  registroServiceImpl.register(usuario));
        
    }


}
