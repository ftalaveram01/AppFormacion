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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.viewnext.core.business.model.Course;
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
	public ResponseEntity<String> create(@RequestBody Course course, @RequestParam(required = true) Long idAdmin, UriComponentsBuilder ucb){
		Long id = courseServices.create(course, idAdmin);
		return ResponseEntity.created(ucb.path("/courses/{id}").build(id)).build();
	}
	
	/**
	 * 
	 * Actualiza el curso dado.
	 * 
	 * @param id
	 * @param course
	 * @return 204
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> update(@RequestBody Course course, @PathVariable Long id, @RequestParam(required = true) Long idAdmin) {
		courseServices.update(course, id, idAdmin);
		return ResponseEntity.ok().build();
	}

	
	/**
	 * 
	 * Borra el {@link Course} dado.
	 * 
	 * @param id
	 * @return 204
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam(required = true) Long idAdmin){
		courseServices.delete(id, idAdmin);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> inscribir(@PathVariable("id") Long idUsuario, @RequestParam(required = true) Long idCurso){
		courseServices.inscribir(idUsuario, idCurso);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUsuario(@PathVariable("id") Long idUsuario, @RequestParam(required = true) Long idCurso){
		courseServices.deleteUsuario(idUsuario, idCurso);
		return ResponseEntity.noContent().build();
	}

}
 