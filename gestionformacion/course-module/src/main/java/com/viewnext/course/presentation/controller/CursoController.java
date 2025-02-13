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

	@GetMapping
	public List<Course> getAll(){
		return courseServices.getAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable Long id){
		Optional<Course> course = courseServices.read(id);
		return course.isPresent() ? ResponseEntity.ok(course.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Course course, UriComponentsBuilder ucb){
		Long id = courseServices.create(course);
		return ResponseEntity.created(ucb.path("/courses/{id}").build(id)).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Course course){
		course.setId(id);
		courseServices.update(course);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		courseServices.delete(id);
		return ResponseEntity.noContent().build();
	}

}
 