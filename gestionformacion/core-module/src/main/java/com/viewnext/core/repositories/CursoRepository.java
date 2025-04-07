package com.viewnext.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.CourseReporte;

@Repository
public interface CursoRepository extends JpaRepository<Course, Long>{

	@Query("SELECT new com.viewnext.core.business.model.CourseReporte("
			+ "c.id, "
			+ "c.nombre, "
			+ "c.numeroHoras, "
			+ "SIZE(c.usuarios), "
			+ "SIZE(c.convocatorias), "
			+ "CASE WHEN c.habilitado = true THEN 'Si' ELSE 'No' END) FROM Course c")
	List<CourseReporte> findAllCourseReportes();
}
