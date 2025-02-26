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
		
		cursoRepository.deleteById(id);
		
	}

	@Override
	public List<Course> getAll() {
		return cursoRepository.findAll();
	}
 
	private boolean isAdmin(Long idAdmin) {
		if(!usuarioRepository.existsById(idAdmin))
			throw new IllegalStateException("No existe el usuario admin");
		
		Usuario usuario = usuarioRepository.findById(idAdmin).get();
		if(usuario.getRol().getNombreRol().equals(RolEnum.ADMIN))
			return true;
		
		return false;
	}

	@Override
	public void addUsuario(Long idUsuario, Long idCurso) {
		// TODO Auto-generated method stub
		
	}

	
	
}