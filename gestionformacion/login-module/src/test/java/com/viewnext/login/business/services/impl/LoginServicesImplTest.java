package com.viewnext.login.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
 class LoginServicesImplTest {
	
	@Mock
    private UsuarioRepository loginRepository;

    @InjectMocks
    private LoginServicesImpl loginServicesImpl;
    
    
    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:mem:testdb");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            return (DataSource)dataSource;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            em.setPackagesToScan("com.viewnext.core.business.model");
            em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            return em;
        }
    }
    
    
    @Test
    void loginTest() {
    	Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail("prueba@email.com");
        user.setPassword("1234");
        user.setHabilitado(true);
    	
    	 when(loginRepository.findByEmailAndPassword("prueba@gmail.com", "1234")).thenReturn(user);
         when(loginRepository.findByEmailAndPassword("error@gmail.com", "12345")).thenReturn(null);

         Optional<Usuario> optional1 = loginServicesImpl.login("prueba@gmail.com", "1234");
         Optional<Usuario> optional2 = loginServicesImpl.login("error@gmail.com", "12345");

         assertTrue(optional1.isPresent());
         assertTrue(optional2.isEmpty());

         assertEquals(1L, optional1.get().getId());
    	
    }
    
}
