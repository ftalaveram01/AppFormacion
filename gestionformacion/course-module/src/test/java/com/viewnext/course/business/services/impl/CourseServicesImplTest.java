package com.viewnext.course.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.viewnext.course.business.model.Course;
import com.viewnext.course.integration.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {
	
	@Mock
	private CursoRepository courseRepository;
	
    @InjectMocks
	private CourseServicesImpl courseServices;
    

    
    @Test
    void createTest() {
    	Course course = new Course();
    	
    	Course coursev2 = new Course();
    	coursev2.setId(2L);
    	
    	when(courseRepository.save(course)).thenReturn(coursev2);
    	
    	Long id = courseServices.create(course);
    	
    	assertEquals(2L, id);
    }
    
    @Test
    void createIdExistenteTest() {
    	Course course = new Course();
    	course.setId(2L);
    	assertThrows(IllegalStateException.class, () -> courseServices.create(course));
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
    	
    	when(courseRepository.existsById(2L)).thenReturn(true);
    	courseServices.update(course);
    	verify(courseRepository, times(1)).save(course);
    }
    
    @Test
    void updateIdNoExistenteTest() {
    	Course course = new Course();
    	course.setId(2L);
    	
    	when(courseRepository.existsById(any(Long.class))).thenReturn(false);
    	assertThrows(IllegalStateException.class, () -> courseServices.update(course));
    	
    	course.setId(null);
    	assertThrows(IllegalStateException.class, () -> courseServices.update(course));
    }
    
    @Test
    void deleteIdExistenteTest() {
    	when(courseRepository.existsById(any(Long.class))).thenReturn(true);
    	courseServices.delete(2L);
    	verify(courseRepository, times(1)).deleteById(2L);
    }
    
    @Test
    void deleteIdNoExistenteTest() {
    	when(courseRepository.existsById(any(Long.class))).thenReturn(false);
    	assertThrows(IllegalStateException.class, () -> courseServices.delete(2L));
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

}
