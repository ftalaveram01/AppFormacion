package com.viewnext.course.presentation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.course.business.model.Course;
import com.viewnext.course.business.services.CourseServices;

@RestController
@RequestMapping("/courses")
public class CursoController {
	
	private CourseServices courseServices;
	
	public CursoController(CourseServices courseServices) {
		this.courseServices = courseServices;
	}

	/**
	 * 
	 * Retorna un 200 con todos los cursos.
	 * 
	 * @return {@link List} de {@link Course}
	 */
	@GetMapping
	public List<Course> getAll(){
		return courseServices.getAll();
	}
	
	/**
	 * 
	 * Dado un id devuelve un 200 con el curso si existe 
	 * o 404 si no existe.
	 * 
	 * @param {@link Long} id
	 * @return 200 con el curso o 404 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Course> read(@PathVariable Long id){
		Optional<Course> course = courseServices.read(id);
		return course.isPresent() ? ResponseEntity.ok(course.get()) : ResponseEntity.notFound().build();
	}
	
	/**
	 * 
	 * Crea un {@link Course} dado.
	 * 
	 * @param course
	 * @param ucb
	 * @return 201 con la ubicación
	 */
	@PostMapping
	public ResponseEntity<String> create(@RequestBody Course course, UriComponentsBuilder ucb){

		try{
			Long id = courseServices.create(course);
		return ResponseEntity.created(ucb.path("/courses/{id}").build(id)).build();
		}catch (IllegalStateException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * Actualiza el curso dado.
	 * 
	 * @param id
	 * @param course
	 * @return 204
	 */
	@PutMapping
	public ResponseEntity<String> update(@RequestBody Course course) {
		try {
			courseServices.update(course);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	
	/**
	 * 
	 * Borra el {@link Course} dado.
	 * 
	 * @param id
	 * @return 204
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id){
		try {
			courseServices.delete(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
 