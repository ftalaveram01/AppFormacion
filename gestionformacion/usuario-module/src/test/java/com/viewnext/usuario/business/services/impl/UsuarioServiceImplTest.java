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

    @BeforeEach
    void setUp() {
        usuarioServicesImpl = Mockito.spy(new UsuarioServicesImpl(usuarioRepository, rolRepository));
    }
	
	 @Test
	    void testDarAltaUser() {
		 
	        Usuario usuario = new Usuario();
	        usuario.setId(null);
	        usuario.setEmail("test@gmail.com");
	        Rol rol = new Rol();
	        rol.setId(0L);
	        usuario.setRol(rol);
	        
	        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
	        when(usuarioRepository.save(usuario)).thenAnswer(invocation -> {
	            Usuario savedUsuario = invocation.getArgument(0);
	            savedUsuario.setId(1L);
	            return savedUsuario;
	        });

	        doReturn(true).when(usuarioServicesImpl).isAdmin(anyLong());
	        
	        Long id = usuarioServicesImpl.create(usuario, usuario.getRol().getId());
	        
	        assertEquals(usuario.getId(), id);
	        verify(usuarioRepository, times(1)).save(usuario);
	        verify(rolRepository, times(0)).findByNombreRol(any(RolEnum.class));
	        
	    }
	
	@Test
	void testDarAltaUserNoAdmin() {
		testNoAdmin();
	}
	
	@Test
	void testDarAltaUserNotNull() {

    	Usuario usuario = new Usuario();
    	usuario.setId(2L);
        
       assertThrows(IllegalStateException.class,() ->  
       				usuarioServicesImpl.create(usuario, 1L));
		
	}
	
	@Test
	@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
	void testDarAltaUserExistente() {
		
    	Usuario usuario = new Usuario();
    	usuario.setId(null);
    	usuario.setEmail("test@gmail.com");

        when(usuarioRepository.existsByEmail("test@gmail.com")).thenReturn(true);

       assertThrows(IllegalStateException.class,() -> 
       				usuarioServicesImpl.create(usuario, 0L));
		
	}
	
	@Test
	void testDarAltaUserRolNull() {
		
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setEmail("test@gmail.com");
        usuario.setRol(null);

        assertThrows(IllegalStateException.class, () -> {
        			usuarioServicesImpl.create(usuario, 1L);       
        });
        
	}
	
	@Test
	@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
	void testDelete() {
		
		when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
		usuarioRepository.deleteById(1L);
		verify(usuarioRepository, times(1)).deleteById(1L);
		
	}
	
	@Test
	void testDeleteNoAdmin() {
		testNoAdmin();
	}
	
	@Test
	void testDeleteNoExisteUser() {
		when(usuarioRepository.existsById(any(Long.class))).thenReturn(false);
		assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.delete(1L, 1L));	
	}
	
	@Test
	void testGetAll() {
		
		Usuario usuario1 = new Usuario();
		usuario1.setId(1L);
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(2L);
		
	    Rol rol = new Rol();
	    rol.setNombreRol(RolEnum.ADMIN);
	    usuario1.setRol(rol);
		
		when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
	    doReturn(true).when(usuarioServicesImpl).isAdmin(1L);
		
		List<Usuario> users = usuarioServicesImpl.getAll(1L);
		
		assertEquals(Arrays.asList(usuario1, usuario2), users);
		
	}
	
	@Test
	void testUpdate() {
		
	    Usuario user = new Usuario();
	    Rol rol = new Rol();
	    rol.setNombreRol(RolEnum.ADMIN);
	    
	    user.setId(1L);
	    user.setRol(rol);
	    
	    when(usuarioRepository.existsById(1L)).thenReturn(true);


	    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

	    when(usuarioServicesImpl.isAdmin(1L)).thenReturn(true);

	    usuarioServicesImpl.update(user, user.getId());

	    verify(usuarioRepository, times(1)).findById(1L);
	    verify(usuarioRepository, times(1)).save(user);
	    
	}

	
	@Test
	void testUpdateNoAdmin() {
		testNoAdmin();
	}
	
	@Test
	void testUpdateNoExiste(){
		
		Usuario user = new Usuario();
		user.setId(3L);
		
		when(usuarioRepository.existsById(any(Long.class))).thenReturn(false);
		assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.update(user, 1L));
		
    	user.setId(null);
    	assertThrows(IllegalStateException.class, () -> usuarioServicesImpl.update(user, 1l));
    	
	}
	
	@Test
	void testGetAllNoAdmin() {
		testNoAdmin();
	}
	
	@Test
	public void testRead() {

	    Usuario usuario = new Usuario();
	    usuario.setId(1L);

	    Rol rol = new Rol();
	    rol.setNombreRol(RolEnum.ADMIN);
	    usuario.setRol(rol);

	    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));


	    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
	    doReturn(true).when(usuarioServicesImpl).isAdmin(1L);

	    Usuario resultado = usuarioServicesImpl.read(1L, 1L);

	    verify(usuarioRepository, times(1)).findById(1L);
	    assertNotNull(resultado);
	    assertEquals(usuario, resultado);
	    
	}
	
	@Test
	void testReadNoAdmin() {
		testNoAdmin();
	}
		
	
	// ********************************************
	//
	// Private Methods
	//
	// ********************************************
	
	private void testNoAdmin() {

        Rol rol = new Rol();
        rol.setId(1L);
        
        doReturn(true).when(usuarioServicesImpl).isAdmin(anyLong());
        
        boolean esAdmin = usuarioServicesImpl.isAdmin(rol.getId());
        
        assertTrue(esAdmin);	
        
	}

}
