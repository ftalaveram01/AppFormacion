package com.viewnext.course.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viewnext.course.business.model.Course;

@Repository
public interface CursoRepository extends JpaRepository<Course, Long>{
	 
}
