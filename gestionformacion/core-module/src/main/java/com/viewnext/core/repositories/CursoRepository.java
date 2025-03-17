package com.viewnext.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Course;

@Repository
public interface CursoRepository extends JpaRepository<Course, Long>{

	
}
