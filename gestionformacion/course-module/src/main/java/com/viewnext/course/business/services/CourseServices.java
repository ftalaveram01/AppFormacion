package com.viewnext.course.business.services;

import java.util.List;
import java.util.Optional;

import com.viewnext.course.business.model.Course;

public interface CourseServices {
 
	Long create(Course course);
	
	Optional<Course> read(Long id);
	
	void update(Course course);
	
	void delete(Long id);
	
	List<Course> getAll();
	
}
