package com.viewnext.course.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viewnext.course.business.model.Course;

public interface CursoRepository extends JpaRepository<Course, Long>{
	 
}
