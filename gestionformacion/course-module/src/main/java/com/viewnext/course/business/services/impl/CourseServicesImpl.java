package com.viewnext.course.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.course.business.model.Course;
import com.viewnext.course.business.services.CourseServices;
import com.viewnext.course.integration.repository.CursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CourseServicesImpl implements CourseServices {
	
	private final CursoRepository cursoRepository;	

	public CourseServicesImpl(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	@Override
	public Long create(Course course) {

		if(course.getId() != null) {
			throw new IllegalStateException("Para crear un curso el id ha de ser null.");
		}
		
		Course createdCourse = cursoRepository.save(course);
		
		return createdCourse.getId();
	}

	@Override
	public Optional<Course> read(Long id) {
	    return Optional.ofNullable(cursoRepository.findById(id).orElse(null));
	}

	@Override
	public void update(Course course) {

		Long id = course.getId();
		
		boolean existe = cursoRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException("El curso con ID [" + id + "] no existe.");
		}
		
		cursoRepository.save(course);
		
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		boolean existe = cursoRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException("El curso con ID [" + id + "] no existe.");
		}
		
		cursoRepository.deleteById(id);
		
	}

	@Override
	public List<Course> getAll() {
		return cursoRepository.findAll();
	}
 
	
	
}