package com.viewnext.course.business.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.CursoRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.course.business.services.CourseServices;

import jakarta.transaction.Transactional;

@Service
public class CourseServicesImpl implements CourseServices {
	
	private final static String ELCURSO = "El curso con ID [";
	
	private final static String NOEXISTE = "] no existe.";
	
	private final CursoRepository cursoRepository;
	
	private final UsuarioRepository usuarioRepository;

	public CourseServicesImpl(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
		this.cursoRepository = cursoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Long create(Course course) {
		
		course.setHabilitado(true);

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

	@Transactional
	@Override
	public void update(Course course, Long id) {
		
		if(course.getId() == null)
			course.setId(id);
		else if(course.getId() != id)
			throw new IllegalStateException("No coincide el id del body con la ruta.");
		
		if(!cursoRepository.existsById(id)) {
			throw new IllegalStateException(ELCURSO + id + NOEXISTE);
		}
		
		course.setHabilitado(true);		
		cursoRepository.save(course);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		boolean existe = cursoRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException(ELCURSO + id + NOEXISTE);
		}
		
		Course curso = cursoRepository.findById(id).get();

		if(Boolean.TRUE.equals(curso.getHabilitado())) {
			curso.setHabilitado(false);
			curso.setUsuarios(new ArrayList<>());
			cursoRepository.save(curso);
		}else
			throw new IllegalStateException("El curso ya está deshabilitado");	
	}

	@Override
	public List<Course> getAll() {
		return cursoRepository.findAll();
	}

	@Override
	public void inscribir(Long idUsuario, Long idCurso) {
		
		Course curso = getCursoById(idCurso);
		Usuario user = getUsuarioById(idUsuario);
		
		if(curso.getUsuarios().contains(user))
			throw new IllegalStateException("El usuario dado ya esta inscrito en el curso.");
		
		curso.getUsuarios().add(user);
		
		cursoRepository.save(curso);
	}

	@Override
	public void deleteUsuario(Long idUsuario, Long idCurso) {
				
		Course curso = getCursoById(idCurso);
		
		if(!usuarioRepository.existsById(idUsuario))
			throw new IllegalStateException("No existe el usuario con id "+ idUsuario);
		
		Boolean borrado = curso.getUsuarios().removeIf(u -> u.getId().equals(idUsuario));
		
		if(!borrado)
			throw new IllegalStateException("El usuario dado no esta inscrito en el curso.");
		
		cursoRepository.save(curso);
	}
	
	private Course getCursoById(Long idCurso) {
		boolean existe = cursoRepository.existsById(idCurso);
		
		if(!existe) {
			throw new IllegalStateException(ELCURSO + idCurso + NOEXISTE);
		}
		
		Optional <Course> cursoOptional = cursoRepository.findById(idCurso);
		
		return cursoOptional.get();
	}
	
	private Usuario getUsuarioById(Long idUsuario) {
		
		if(!usuarioRepository.existsById(idUsuario))
			throw new IllegalStateException("No existe el usuario con id "+ idUsuario);
		
		Optional <Usuario> userOptional = usuarioRepository.findById(idUsuario);
		
		return userOptional.get();
	}
	
	

	
	
}