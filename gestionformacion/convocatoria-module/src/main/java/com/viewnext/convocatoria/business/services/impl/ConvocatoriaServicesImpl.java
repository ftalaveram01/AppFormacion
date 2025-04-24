package com.viewnext.convocatoria.business.services.impl;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.viewnext.convocatoria.business.services.ConvocatoriaScheduler;
import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaEnum;
import com.viewnext.core.business.model.ConvocatoriaReporte;
import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.ConvocatoriaRepository;
import com.viewnext.core.repositories.CursoRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.procesador.HolaMundo;

import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConvocatoriaServicesImpl implements ConvocatoriaServices {
	
	private ConvocatoriaRepository convocatoriaRepository;
	
	private CursoRepository cursoRepository;
	
	private ConvocatoriaScheduler convocatoriaScheduler;
	
	private UsuarioRepository usuarioRepository;
	
	private EmailService emailService;
	
	public ConvocatoriaServicesImpl(ConvocatoriaRepository convocatoriaRepository,
			ConvocatoriaScheduler convocatoriaScheduler, CursoRepository cursoRepository, UsuarioRepository usuarioRepository, EmailService emailService) {
		this.convocatoriaRepository = convocatoriaRepository;
		this.convocatoriaScheduler = convocatoriaScheduler;
		this.cursoRepository = cursoRepository;
		this.usuarioRepository = usuarioRepository;
		this.emailService = emailService;
	}

	@HolaMundo
    @Transactional
	@Override
	public Convocatoria create(ConvocatoriaRequest request) {
		
		if(request.getFechaFin().before(request.getFechaInicio()))
			throw new IllegalStateException("La fecha de inicio no puede ser despues de la de fin.");
		
		if(request.getFechaInicio().before(new Date()))
			throw new IllegalStateException("La fecha de inicio no puede ser antes de la actual.");
		
		if(!cursoRepository.existsById(request.getIdCurso()))
			throw new IllegalStateException("No existe el curso de la convocatoria");
		
		Course curso = cursoRepository.findById(request.getIdCurso()).get();
		
		if(Boolean.FALSE.equals(curso.getHabilitado()))
			throw new IllegalStateException("El curso está deshabilitado");
		
		if(curso.getUsuarios().size()<10)
			throw new IllegalStateException("No hay suficientes alumnos inscritos al curso.");
		
		Convocatoria conv = new Convocatoria();
		conv.setFechaInicio(request.getFechaInicio());
		conv.setFechaFin(request.getFechaFin());
		conv.setCurso(curso);
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		conv.setUsuarios(new ArrayList<>());
		
		Convocatoria guardada = convocatoriaRepository.save(conv);
		convocatoriaScheduler.programarTarea(guardada, true, false);
		
		enviarCorreo(guardada);	
		
		return guardada;
	}

	@Override
	public List<Convocatoria> getAll() {
		
		return convocatoriaRepository.findAll();
	}

	@Override
	public List<Convocatoria> getActivas() {
		
		EnumSet<ConvocatoriaEnum> estadosActivos = EnumSet.of(
	            ConvocatoriaEnum.CONVOCADA,
	            ConvocatoriaEnum.EN_CURSO,
	            ConvocatoriaEnum.EN_PREPARACION
	        );
		
		return convocatoriaRepository
				.findAll().stream()
						  .filter(c ->estadosActivos.contains(c.getEstado()))
						  .toList();
	}

    @Transactional
	@Override
	public void update(Long id, UpdateRequest request) {
		
		if(request.getFechaFin().before(request.getFechaInicio()))
			throw new IllegalStateException("La fecha de inicio no puede ser despues de la de fin.");
		
		if(request.getFechaInicio().before(new Date()))
			throw new IllegalStateException("La fecha de inicio no puede ser antes de la actual.");
		
		if(!convocatoriaRepository.existsById(id))
			throw new IllegalStateException("No existe la convocatoria.");
		
		Convocatoria conv = convocatoriaRepository.findById(id).get();
		if(conv.getEstado().equals(ConvocatoriaEnum.TERMINADA) || conv.getEstado().equals(ConvocatoriaEnum.DESIERTA))
			throw new IllegalStateException("La convocatoria no está activa");
		conv.setFechaInicio(request.getFechaInicio());
		conv.setFechaFin(request.getFechaFin());
		conv.setUsuarios(new ArrayList<>());
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		
		convocatoriaRepository.save(conv);
		
		convocatoriaScheduler.programarTarea(conv, true, false);
		
		this.enviarCorreo(conv);
	}

    @Transactional
	@Override
	public void delete(Long id) {
		
		if(!convocatoriaRepository.existsById(id))
			throw new IllegalStateException("No existe la convocatoria.");
		
		Convocatoria conv = convocatoriaRepository.findById(id).get();
		
		if(conv.getEstado().equals(ConvocatoriaEnum.TERMINADA) || conv.getEstado().equals(ConvocatoriaEnum.DESIERTA))
			throw new IllegalStateException("La convocatoria ya está cancelada o está terminada");
		
		conv.setEstado(ConvocatoriaEnum.DESIERTA);
		
		convocatoriaRepository.save(conv);
		
		convocatoriaScheduler.cancelarTareas(conv);
		
	}

	@Override
	public List<Convocatoria> getFromUsuario(Long idUsuario) {
		if(!usuarioRepository.existsById(idUsuario)) {
			throw new IllegalStateException("ERROR el usuario no existe");
		}
		
		return convocatoriaRepository.findAll().stream()
											   .filter(c -> c.getUsuarios().stream()
	                                                   						.anyMatch(u -> u.getId().equals(idUsuario)))
			                                   .collect(Collectors.toList());
	}

    @Transactional
	@Override
	public void inscribirUsuario(Long idConvocatoria, Long idUsuario) {
		
		Convocatoria convocatoria = convocatoriaRepository.findById(idConvocatoria)
				.orElseThrow(() -> new IllegalStateException("ERROR la convocatoria no existe"));
		
		Usuario user = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new IllegalStateException("ERROR el usuario a inscribir no existe"));
		
		if(convocatoria.getEstado().equals(ConvocatoriaEnum.DESIERTA) || convocatoria.getEstado().equals(ConvocatoriaEnum.TERMINADA))
			throw new IllegalStateException("No puedes inscribir un usuario en una convocatoria que no esta activa.");
		
		if(convocatoria.getUsuarios().contains(user)) {
			throw new IllegalStateException("ERROR: El usuario ya está inscrito en la convocatoria");
		}
		
		if(convocatoria.getUsuarios().size()==15)
			throw new IllegalStateException("No quedan plazas en la convocatoria.");
		
		convocatoria.getUsuarios().add(user);
		
		if(convocatoria.getUsuarios().size()==15)
			convocatoria.setEstado(ConvocatoriaEnum.CONVOCADA);
		
		convocatoriaRepository.save(convocatoria);
		
	}

	@Override
	public void generarCertificado(Long idConvocatoria, Long idUsuario) {

		if(!convocatoriaRepository.existsById(idConvocatoria)) {
			throw new IllegalStateException("La convocatoria con id ["+idConvocatoria+"], NO EXISTE");
		}

		if(!usuarioRepository.existsById(idUsuario)) {
			throw new IllegalStateException("El usuario con id ["+idUsuario+"], NO EXISTE");
		}
		
		Optional <Convocatoria> convocatoria = convocatoriaRepository.findById(idConvocatoria);
		Optional <Usuario> usuario = usuarioRepository.findById(idUsuario);
		
		this.emailService.enviarCorreo(
				usuario.get().getEmail(), 
				"ENHORABUENA, AQUI TIENES TU CERTIFICADO", 
				"Has completado el curso de "+convocatoria.get().getCurso().getNombre() + "\n" + "Fecha Inicio: "+convocatoria.get().getFechaInicio() + "\n" + "Fecha Fin: "+convocatoria.get().getFechaFin());

		
	}
	
	private void enviarCorreo(Convocatoria convocatoria) {
		
		for (Usuario usuario: convocatoria.getCurso().getUsuarios()) {
			this.emailService.enviarCorreo(usuario.getEmail(), 
					"Convocatoria para el curso "+convocatoria.getCurso().getNombre(), 
					"Acepta la convocatoria en el siguiente apartado." + "\n" + "http://localhost:4200/confirmacion?idConvocatoria="+convocatoria.getId().toString()+"&idUsuario="+usuario.getId().toString());
        }
	}

	@Override
	public byte[] generarReporte() {
	    List<ConvocatoriaReporte> convocatorias = convocatoriaRepository.findAllConvocatoriaReportes();

	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(convocatorias);

	    JasperReport jasperReport = null;
	    JasperPrint jasperPrint = null;
	    byte[] resultado = null;

	    InputStream reportStream = getClass().getResourceAsStream("/convocatoria.jrxml");
	    try {
	        jasperReport = JasperCompileManager.compileReport(reportStream);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    Map<String, Object> parameters = new HashMap<>();
	    parameters.put("ReportTitle", "Convocatoria Report");

	    try {
	        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	        resultado = JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    try (FileOutputStream fos = new FileOutputStream("ReporteConvocatoria.pdf")) {
	        fos.write(resultado);
	    } catch (IOException e) {
	        System.out.println("Error saving PDF to file: " + e.getMessage());
	    }

	    return resultado;
	}
	
}
