package com.viewnext.login.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.viewnext.login.business.model.Usuario;
import com.viewnext.login.integration.repository.LoginRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServicesImplTest {
	
	@Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginServicesImpl loginServicesImpl;

    private Usuario user;
    
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup(){
    	initObject();
        MockitoAnnotations.initMocks(this);
        loginServicesImpl= new LoginServicesImpl(loginRepository);
    }
    
    @Test
    void loginTest() {
    	
    	 when(loginRepository.findByEmailAndPassword("prueba@gmail.com", "1234")).thenReturn(user);
         when(loginRepository.findByEmailAndPassword("error@gmail.com", "12345")).thenReturn(null);

         Optional<Usuario> optional1 = loginServicesImpl.login("prueba@gmail.com", "1234");
         Optional<Usuario> optional2 = loginServicesImpl.login("error@gmail.com", "12345");

         assertTrue(optional1.isPresent());
         assertTrue(optional2.isEmpty());

         assertEquals(1L, optional1.get().getId());
    	
    }
    
    //************************************
    //
    // Private method
    //
    //************************************

    private void initObject() {
        user = new Usuario();
        user.setId(1L);
        user.setEmail("prueba@email.com");
        user.setPassword("1234");
    }
    
}
