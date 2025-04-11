package com.viewnext.course.business.services.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaEnum;
import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.CourseReporte;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.ConvocatoriaRepository;
import com.viewnext.core.repositories.CursoRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.course.business.services.CourseServices;

import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CourseServicesImpl implements CourseServices {
	
	private final static String ELCURSO = "El curso con ID [";
	
	private final static String NOEXISTE = "] no existe.";
	
	private final CursoRepository cursoRepository;
	
	private final UsuarioRepository usuarioRepository;
	
	private final ConvocatoriaRepository convocatoriaRepository;

	public CourseServicesImpl(CursoRepository cursoRepository, UsuarioRepository usuarioRepository, ConvocatoriaRepository convocatoriaRepository) {
		this.cursoRepository = cursoRepository;
		this.usuarioRepository = usuarioRepository;
		this.convocatoriaRepository = convocatoriaRepository;	
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
			
			List<Convocatoria> convocatorias = curso.getConvocatorias();
			convocatorias.stream().filter(c -> !( 
					c.getEstado().equals(ConvocatoriaEnum.TERMINADA) || 
					c.getEstado().equals(ConvocatoriaEnum.DESIERTA)) )
					.forEach(c -> c.setEstado(ConvocatoriaEnum.DESIERTA));
			convocatoriaRepository.saveAll(convocatorias);
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

	@Override
	public byte[] generarReporte() {
		List<CourseReporte> cursos = cursoRepository.findAllCourseReportes();

	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cursos);

	    JasperReport jasperReport = null;
	    JasperPrint jasperPrint = null;
	    byte[] resultado = null;

	    InputStream reportStream = getClass().getResourceAsStream("/course.jrxml");
	    try {
	        jasperReport = JasperCompileManager.compileReport(reportStream);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    Map<String, Object> parameters = new HashMap<>();
	    parameters.put("ReportTitle", "Reporte de Cursos");

	    try {
	        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	        resultado = JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    try (FileOutputStream fos = new FileOutputStream("ReporteCursos.pdf")) {
	        fos.write(resultado);
	    } catch (IOException e) {
	        System.out.println("Error saving PDF to file: " + e.getMessage());
	    }

	    return resultado;
	}
	
	

	
	
}