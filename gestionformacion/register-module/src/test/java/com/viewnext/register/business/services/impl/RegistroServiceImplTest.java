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

import com.viewnext.register.business.model.Usuario;
import com.viewnext.register.integration.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class RegistroServiceImplTest {

	@Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RegistroServiceImpl registroServiceImpl;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup(){
        initObjects();
        MockitoAnnotations.initMocks(this);
        registroServiceImpl= new RegistroServiceImpl(usuarioRepository);
    }

    private Usuario usuario;

    @Test
    void testRegisterNuevoUsuario(){


        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Long idRegistro = registroServiceImpl.register(usuario.getEmail(), usuario.getPassword());

        assertNotNull(idRegistro);
        assertEquals(usuario.getId(), idRegistro);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));

    }

    @Test
    void testRegisterUsuarioExistente(){

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

       assertThrows(IllegalStateException.class,() ->  registroServiceImpl.register(usuario.getEmail(), usuario.getPassword()));
    }

    @Test
    void testRegisterEmailNull(){

        usuario.setEmail(null);

       assertThrows(NullPointerException.class,() ->  registroServiceImpl.register(usuario.getEmail(), usuario.getPassword()));
    }

    @Test
    void testRegisterPasswordNull(){

        usuario.setPassword(null);

       assertThrows(NullPointerException.class,() ->  registroServiceImpl.register(usuario.getEmail(), usuario.getPassword()));
    }

    private void initObjects(){
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("prueba@email.com");
        usuario.setPassword("1234");
    }

}
