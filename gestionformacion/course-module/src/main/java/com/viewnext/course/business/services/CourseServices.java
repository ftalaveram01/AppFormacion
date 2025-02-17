package com.viewnext.course.business.services;

import java.util.List;
import java.util.Optional;

import com.viewnext.course.business.model.Course;

public interface CourseServices {
 
	/**
	 * 
	 * Crea el {@link Course} dado si su id es nulo,
	 * caso contrario lanza IllegalStateException.
	 * 
	 * @param course
	 * @return id
	 */
	Long create(Course course);
	
	/**
	 * 
	 * Retorna un optional del {@link Course} de id dado
	 * o null si no existe.
	 * 
	 * @param id
	 * @return optional del {@link Course}
	 */
	Optional<Course> read(Long id);
	
	/**
	 * 
	 * Si el {@link Course} dado existe lo actualiza,
	 * caso contrario lanza IllegalStateException.
	 * 
	 * @param course
	 */
	void update(Course course);
	
	/**
	 * 
	 * Dado el id borra el {@link Course} si existe,
	 * caso contrario lanza IllegalStateException.
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 
	 * Retorna todos los {@link Course} existentes.
	 * 
	 * @return {@link List} de {@link Course}
	 */
	List<Course> getAll();
	
}
