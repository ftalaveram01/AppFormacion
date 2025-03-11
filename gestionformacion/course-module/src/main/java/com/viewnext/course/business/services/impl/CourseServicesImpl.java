package com.viewnext.course.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.RolEnum;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.course.business.services.CourseServices;
import com.viewnext.course.integration.repository.CursoRepository;
import com.viewnext.course.integration.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class CourseServicesImpl implements CourseServices {
	
	private final CursoRepository cursoRepository;
	
	private final UsuarioRepository usuarioRepository;

	public CourseServicesImpl(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
		this.cursoRepository = cursoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Long create(Course course, Long idAdmin) {
		System.out.println(course.getFechaInicio());
		System.out.println(course.getFechaFin());
		course.setHabilitado(true);
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");

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
	public void update(Course course, Long id, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		if(course.getId() == null)
			course.setId(id);
		else if(course.getId() != id)
			throw new IllegalStateException("No coincide el id del body con la ruta.");
		
		if(!cursoRepository.existsById(id)) {
			throw new IllegalStateException("El curso con ID [" + id + "] no existe.");
		}
		
		course.setHabilitado(true);		
		cursoRepository.save(course);
	}

	@Override
	@Transactional
	public void delete(Long id, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		boolean existe = cursoRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException("El curso con ID [" + id + "] no existe.");
		}
		
		Course curso = cursoRepository.findById(id).get();
		
		if(curso.getHabilitado()) {
			curso.setHabilitado(false);
			cursoRepository.save(curso);
		}else
			throw new IllegalStateException("El curso ya está deshabilitado");	
	}

	@Override
	public List<Course> getAll() {
		return cursoRepository.findAll();
	}
 
	private boolean isAdmin(Long idAdmin) {
		if(!usuarioRepository.existsById(idAdmin))
			throw new IllegalStateException("No existe el usuario admin");
		return usuarioRepository.isAdmin(idAdmin);
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
			throw new IllegalStateException("El curso con ID [" + idCurso + "] no existe.");
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