package com.viewnext.course.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.course.integration.repository.CursoRepository;
import com.viewnext.course.integration.repository.UsuarioRepository;
import com.viewnext.course.presentation.controller.CursoController;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {
	
	@Mock
	private CursoRepository courseRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
    @InjectMocks
	private CourseServicesImpl courseServices;
    

    
    @Test
    void createTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(courseRepository.save(any(Course.class))).thenReturn(course);
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	Long id = courseServices.create(new Course(), 5L);
    	
    	assertEquals(2L, id);
    }
    
    @Test
    void createIdExistenteTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.create(course, 5L));
    }
    
    @Test
    void readTest() {
    	when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    	when(courseRepository.findById(2L)).thenReturn(Optional.of(new Course()));
    	
    	assertTrue(courseServices.read(2L).isPresent());
    	assertTrue(courseServices.read(3L).isEmpty());

    }
    
    @Test
    void updateIdExistenteTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(courseRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	courseServices.update(course, 2L, 5L);
    	
    	verify(courseRepository, times(1)).save(course);
    }
    
    @Test
    void updateIdNoExistenteTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(courseRepository.existsById(any(Long.class))).thenReturn(false);
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.update(course, 2L, 5L));
    }
    
    @Test
    void updateIdNullTest() {
    	Course course = new Course();
    	course.setId(null);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.update(course, 2L, 5L));
    }
    
    @Test
    void updateIdNoCoincideTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.update(course, 3L, 5L));
    }
    
    @Test
    void deleteIdExistenteTest() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(courseRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	courseServices.delete(2L, 5L);
    	
    	verify(courseRepository, times(1)).deleteById(2L);
    }
    
    @Test
    void deleteIdNoExistenteTest() {
    	Usuario admin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ADMIN);
    	admin.setRol(rol);
    	
    	when(courseRepository.existsById(any(Long.class))).thenReturn(false);
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.delete(2L, 5L));
    }
    
    @Test
    void testGetAll() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	Course coursev2 = new Course();
    	coursev2.setId(3L);
    	
    	when(courseRepository.findAll()).thenReturn(Arrays.asList(course, coursev2));
    	List<Course> courses = courseServices.getAll();
    	
    	assertEquals(Arrays.asList(course, coursev2), courses);
    }
    
    @Test
    void testIsNotAdmin() {
    	Usuario noAdmin = new Usuario();
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ALUMNO);
    	noAdmin.setRol(rol);
    	
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(true);
    	when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(noAdmin));
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.delete(2L, 5L));
    	assertThrows(IllegalStateException.class, () -> courseServices.update(new Course(), 2L, 5L));
    	assertThrows(IllegalStateException.class, () -> courseServices.create(new Course(), 5L));
    	
    }
    
    @Test
    void testAdminUserNotExists() {
    	
    	when(usuarioRepository.existsById(any(Long.class))).thenReturn(false);
    	
    	assertThrows(IllegalStateException.class, () -> courseServices.delete(2L, 5L));
    	assertThrows(IllegalStateException.class, () -> courseServices.update(new Course(), 2L, 5L));
    	assertThrows(IllegalStateException.class, () -> courseServices.create(new Course(), 5L));
    	
    }
    
    @Test

    void testAddUserToCourse() {
    	
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ALUMNO);
    	
    	Usuario user = new Usuario();
    	user.setId(1L);
    	user.setRol(rol);
    	
    	Usuario user1 = new Usuario();
    	user1.setId(1L);
    	user1.setRol(rol);
    	
    	Usuario user2 = new Usuario();
    	user2.setId(1L);
    	user2.setRol(rol);
    	
    	List<Usuario> list = new ArrayList<Usuario>();
    	list.add(user);
    	list.add(user1);
    	
    	Course curso = new Course();
    	
    	curso.setId(2L);
    	curso.setNombre("Curso Testing 2");
    	curso.setUsuarios(list);
    	
    	when(usuarioRepository.existsById(user2.getId())).thenReturn(true);
    	when(courseRepository.existsById(curso.getId())).thenReturn(true);
    	when(courseRepository.findById(curso.getId())).thenReturn(Optional.of(curso));
    	when(usuarioRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
    	
    	courseServices.inscribir(user2.getId(),curso.getId());
    	
    	assertEquals(3,curso.getUsuarios().size());
    }
    
    @Test
    void testGetAllUsersByCourse() {
    	
    	Course curso = new Course();
    	curso.setId(1L);
    	
    	Rol rol = new Rol();
    	rol.setNombreRol(RolEnum.ALUMNO);
    	
    	Usuario user = new Usuario();
    	user.setId(1L);
    	user.setRol(rol);
    	
    	Usuario user1 = new Usuario();
    	user1.setId(1L);
    	user1.setRol(rol);
    	
    	Usuario user2 = new Usuario();
    	user2.setId(1L);
    	user2.setRol(rol);
    	
    	List<Usuario> list = new ArrayList<Usuario>();
    	list.add(user);
    	list.add(user1);
    	
    	curso.setUsuarios(list);
    	
    	when(courseRepository.findById(curso.getId())).thenReturn(Optional.of(curso));
    	
    	Optional<Course> optional = courseServices.read(curso.getId());
    	
        assertTrue(optional.isPresent());
        
        assertEquals(2, optional.get().getUsuarios().size());
        assertTrue(optional.get().getUsuarios().contains(user));
        assertTrue(optional.get().getUsuarios().contains(user1));
    }
    
    @Test	
    void deleteAlumnoCourseIdExiste() {
    	

        Long idUsuario = 1L;
        Long idCurso = 2L;

        Usuario user = new Usuario();
        user.setId(idUsuario);

        Course curso = new Course();
        curso.setId(idCurso);
        curso.setUsuarios(new ArrayList<>(List.of(user)));

    	
    	when(usuarioRepository.existsById(user.getId())).thenReturn(true);
    	when(courseRepository.existsById(curso.getId())).thenReturn(true);

        when(courseRepository.findById(idCurso)).thenReturn(Optional.of(curso));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(user));

        courseServices.deleteUsuario(user.getId(), curso.getId());

        assertFalse(curso.getUsuarios().contains(user));

       }

}
