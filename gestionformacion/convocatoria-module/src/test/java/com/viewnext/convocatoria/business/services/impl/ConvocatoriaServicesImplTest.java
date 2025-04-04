package com.viewnext.convocatoria.business.services.impl;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.viewnext.convocatoria.business.services.ConvocatoriaScheduler;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaEnum;
import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.ConvocatoriaRepository;
import com.viewnext.core.repositories.CursoRepository;
import com.viewnext.core.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ConvocatoriaServicesImplTest {
	
	@Mock
	private ConvocatoriaRepository convocatoriaRepository;
	
	@Mock
	private CursoRepository cursoRepository;
	
	@Mock
	private ConvocatoriaScheduler convocatoriaScheduler;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private EmailService emailService;
	
	@InjectMocks
	private ConvocatoriaServicesImpl convocatoriaServices;
	
	@Test
	void inscribirUsuarioConvocatoriaInexistente() {

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.empty());
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.inscribirUsuario(5L, 2L));
		
	}
	
	@Test
	void inscribirUsuarioInexistente() {
		Convocatoria conv = new Convocatoria();

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.inscribirUsuario(5L, 2L));
		
	}
	
	@Test
	void inscribirUsuarioYaInscrito() {
		Convocatoria conv = new Convocatoria();
		Usuario usu = new Usuario();
		usu.setId(2L);
		conv.setUsuarios(Arrays.asList(usu));
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usu));
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.inscribirUsuario(5L, 2L));
		
	}
	
	@Test
	void inscribirUsuarioConvocatoriaLlena() {
		Convocatoria conv = new Convocatoria();
		Usuario usu = new Usuario();
		usu.setId(2L);
		List<Usuario> lista = new ArrayList<>();
		
		for(int i=0;i<15;i++)
			lista.add(new Usuario());
		conv.setUsuarios(lista);
		conv.setId(5L);
		conv.setEstado(ConvocatoriaEnum.CONVOCADA);

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usu));
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.inscribirUsuario(5L, 2L));
		
	}
	
	@Test
	void inscribirUsuarioOk() {
		Convocatoria conv = new Convocatoria();
		conv.setUsuarios(new ArrayList<Usuario>());
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		Usuario usu = new Usuario();
		usu.setId(2L);

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usu));
		
		convocatoriaServices.inscribirUsuario(5L,  2L);
		
		conv.setUsuarios(Arrays.asList(usu));
		
		verify(convocatoriaRepository, times(1)).save(conv);
		
	}
	
	@Test
	void inscribirUsuarioOkLlena() {
		Convocatoria conv = new Convocatoria();
		Usuario usu = new Usuario();
		usu.setId(2L);
		
		List<Usuario> lista = new ArrayList<>();
		
		for(int i=0;i<14;i++)
			lista.add(new Usuario());
		conv.setUsuarios(lista);
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);

		when(convocatoriaRepository.findById(5L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usu));
		
		convocatoriaServices.inscribirUsuario(5L,  2L);
		
		lista.add(usu);
		conv.setUsuarios(lista);
		conv.setEstado(ConvocatoriaEnum.CONVOCADA);
		
		verify(convocatoriaRepository, times(1)).save(conv);
		
	}

	
	@Test
	void deleteNoExistente() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.delete(2L));
	}
	
	@Test
	void deleteInactiva() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(true);
		
		Convocatoria conv = new Convocatoria();
		conv.setEstado(ConvocatoriaEnum.TERMINADA);
		when(convocatoriaRepository.findById(2L)).thenReturn(Optional.of(conv));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.delete(2L));
		
		conv.setEstado(ConvocatoriaEnum.DESIERTA);
		when(convocatoriaRepository.findById(2L)).thenReturn(Optional.of(conv));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.delete(2L));
	}
	
	@Test
	void deleteOk() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(true);
		
		Convocatoria conv = new Convocatoria();
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		when(convocatoriaRepository.findById(2L)).thenReturn(Optional.of(conv));
		
		convocatoriaServices.delete(2L);
		
		conv.setEstado(ConvocatoriaEnum.DESIERTA);
		
		verify(convocatoriaRepository, times(1)).save(conv);
		verify(convocatoriaScheduler, times(1)).cancelarTareas(conv);
	}

	
	@Test
	void testGetAll() {
		Convocatoria conv = new Convocatoria();
		Convocatoria conv2 = new Convocatoria();
		conv.setId(1L);
		conv2.setId(2L);
		
		when(convocatoriaRepository.findAll()).thenReturn(Arrays.asList(conv, conv2));
		
		List<Convocatoria> convs = convocatoriaServices.getAll();
		
		assertTrue(convs.containsAll(Arrays.asList(conv, conv2)));
	}
	
	@Test
	void testGetActivas() {
		Convocatoria conv = new Convocatoria();
		Convocatoria conv2 = new Convocatoria();
		Convocatoria conv3 = new Convocatoria();
		Convocatoria conv4 = new Convocatoria();
		Convocatoria conv5 = new Convocatoria();
		conv.setId(1L);
		conv2.setId(2L);
		conv3.setId(3L);
		conv4.setId(4L);
		conv5.setId(5L);
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		conv2.setEstado(ConvocatoriaEnum.CONVOCADA);
		conv3.setEstado(ConvocatoriaEnum.EN_CURSO);
		conv4.setEstado(ConvocatoriaEnum.TERMINADA);
		conv5.setEstado(ConvocatoriaEnum.DESIERTA);
		
		when(convocatoriaRepository.findAll()).thenReturn(Arrays.asList(conv, conv2, conv3, conv5, conv5));
		
		List<Convocatoria> convs = convocatoriaServices.getActivas();
		assertTrue(convs.containsAll(Arrays.asList(conv, conv2, conv3)));
		assertFalse(convs.contains(conv4) || convs.contains(conv5));
		
	}
	
	@Test
	void testGetFromUsuarioNoExistente() {
		when(usuarioRepository.existsById(5L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.getFromUsuario(5L));
	}
	
	@Test
	void testGetFromUsuario() {
		when(usuarioRepository.existsById(5L)).thenReturn(true);
		
		Usuario usu = new Usuario();
		usu.setId(5L);
		Convocatoria conv = new Convocatoria();
		Convocatoria conv2 = new Convocatoria();
		conv.setId(1L);
		conv2.setId(2L);
		conv.setUsuarios(new ArrayList<Usuario>());
		conv2.setUsuarios(Arrays.asList(usu));
		
		when(convocatoriaRepository.findAll()).thenReturn(Arrays.asList(conv, conv2));
		
		List<Convocatoria> convs = convocatoriaServices.getFromUsuario(5L);
		
		assertTrue(convs.contains(conv2));
		assertFalse(convs.contains(conv));
	}
	
	@Test
	void testCreateFechaFinAntes() {
		
		ConvocatoriaRequest request = new ConvocatoriaRequest();
		request.setFechaFin(new Date());
		request.setFechaInicio(new Date(request.getFechaFin().getTime() + 800));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.create(request));
		
	}
	
	@Test
	void testCreateFechaInicioAntesActual() {
		
		ConvocatoriaRequest request = new ConvocatoriaRequest();
		request.setFechaFin(new Date());
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 800));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.create(request));
		
	}
	
	@Test
	void testCreateCursoNoExistente() {
		
		
		ConvocatoriaRequest request = new ConvocatoriaRequest();
		request.setFechaFin(new Date(new Date().getTime() + 2000));
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
		request.setIdCurso(2L);
		
		when(cursoRepository.existsById(2L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.create(request));
		
	}
	
	@Test
	void testCreateAlumnosInsuficientes() {
		
		
		ConvocatoriaRequest request = new ConvocatoriaRequest();
		request.setFechaFin(new Date(new Date().getTime() + 2000));
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
		request.setIdCurso(2L);
		
		when(cursoRepository.existsById(2L)).thenReturn(true);
		
		Course curso = new Course();
		curso.setUsuarios(new ArrayList<Usuario>());
		
		when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.create(request));
		
	}
	
	@Test
	void testCreateOk() {
		
		Usuario usu = new Usuario();
		usu.setEmail("prueba@gmail.com");
		usu.setId(3L);
	    
	    ConvocatoriaRequest request = new ConvocatoriaRequest();
	    request.setFechaFin(new Date(new Date().getTime() + 2000));
	    request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
	    request.setIdCurso(2L);
	    
	    when(cursoRepository.existsById(2L)).thenReturn(true);
	    Course curso = new Course();
	    curso.setUsuarios(new ArrayList<>());
	    List<Usuario> lista = new ArrayList<>();
	    for(int i=0;i<14;i++)
	        lista.add(usu);
	    curso.setUsuarios(lista);
	    curso.setHabilitado(true);
	    curso.setNombre("Curso de prueba");
	    when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
	    
	    Convocatoria conv = new Convocatoria();
	    conv.setId(2L);
	    conv.setFechaInicio(request.getFechaInicio());
	    conv.setFechaFin(request.getFechaFin());
	    conv.setCurso(curso);
	    conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
	    conv.setUsuarios(new ArrayList<>());
	    when(convocatoriaRepository.save(any(Convocatoria.class))).thenReturn(conv);
	    
	    convocatoriaServices.create(request);
	    verify(convocatoriaScheduler, times(1)).programarTarea(conv, true, false);
	    verify(emailService, times(14)).enviarCorreo(usu.getEmail(), 
	    		"Convocatoria para el curso " + conv.getCurso().getNombre(), 
	    		"Acepta la convocatoria en el siguiente apartado." + "\n" + "http://localhost:4200/confirmacion?idConvocatoria="+conv.getId().toString()+"&idUsuario="+usu.getId().toString()); 
	}
	
	@Test
	void testUpdateFechaFinAntes() {
		
		UpdateRequest request = new UpdateRequest();
		request.setFechaFin(new Date());
		request.setFechaInicio(new Date(request.getFechaFin().getTime() + 800));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.update(3L, request));
		
	}
	
	@Test
	void testUpdateFechaInicioAntesActual() {
		
		UpdateRequest request = new UpdateRequest();
		request.setFechaFin(new Date());
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 800));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.update(3L, request));
		
	}
	
	@Test
	void testUpdateConvocatoriaNoExistente() {
		
		
		UpdateRequest request = new UpdateRequest();
		request.setFechaFin(new Date(new Date().getTime() + 2000));
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
		
		when(convocatoriaRepository.existsById(3L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.update(3L, request));
		
	}

	@Test
	void testUpdateNoActiva() {
		
		UpdateRequest request = new UpdateRequest();
		request.setFechaFin(new Date(new Date().getTime() + 2000));
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
		
		when(convocatoriaRepository.existsById(3L)).thenReturn(true);
		
	    Convocatoria conv = new Convocatoria();
	    conv.setEstado(ConvocatoriaEnum.TERMINADA);
	    when(convocatoriaRepository.findById(3L)).thenReturn(Optional.of(conv));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.update(3L, request));
		
	    conv.setEstado(ConvocatoriaEnum.DESIERTA);
	    when(convocatoriaRepository.findById(3L)).thenReturn(Optional.of(conv));
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.update(3L, request));
		
	}
	
	@Test
	void testUpdateOk() {
		
		UpdateRequest request = new UpdateRequest();
		request.setFechaFin(new Date(new Date().getTime() + 2000));
		request.setFechaInicio(new Date(request.getFechaFin().getTime() - 200));
		
		when(convocatoriaRepository.existsById(3L)).thenReturn(true);
		
	    Convocatoria conv = new Convocatoria();
	    Course curso = new Course();
	    curso.setUsuarios(new ArrayList<Usuario>());
	    conv.setEstado(ConvocatoriaEnum.CONVOCADA);
	    conv.setCurso(curso);
	    when(convocatoriaRepository.findById(3L)).thenReturn(Optional.of(conv));
	    
		conv.setFechaInicio(request.getFechaInicio());
		conv.setFechaFin(request.getFechaFin());
		conv.setUsuarios(new ArrayList<Usuario>());
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		
		convocatoriaServices.update(3L, request);
		
		verify(convocatoriaRepository, times(1)).save(conv);
		verify(convocatoriaScheduler, times(1)).programarTarea(conv, true, false);
	}
	
	@Test
	void testGenerarCertificadoConvocatoriaInexistente() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.generarCertificado(2L,  3L));
	}
	
	@Test
	void testGenerarCertificadoUsuarioInexistente() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(true);
		when(usuarioRepository.existsById(3L)).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> convocatoriaServices.generarCertificado(2L,  3L));
	}
	
	@Test
	void testGenerarCertificadoOk() {
		when(convocatoriaRepository.existsById(2L)).thenReturn(true);
		when(usuarioRepository.existsById(3L)).thenReturn(true);
		
		Course curso = new Course();
		curso.setNombre("Prueba");
		Usuario usu = new Usuario();
		usu.setEmail("Prueba@gmail.com");
		Convocatoria conv = new Convocatoria();
		conv.setCurso(curso);
		conv.setFechaInicio(new Date(new Date().getTime() - 200));
		conv.setFechaFin(new Date(new Date().getTime() + 200));
		
		when(convocatoriaRepository.findById(2L)).thenReturn(Optional.of(conv));
		when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usu));
		
		convocatoriaServices.generarCertificado(2L,  3L);
		
		verify(emailService, times(1)).enviarCorreo(usu.getEmail(), 
		"ENHORABUENA, AQUI TIENES TU CERTIFICADO", 
		"Has completado el curso de "+conv.getCurso().getNombre() + "\n" + "Fecha Inicio: "+conv.getFechaInicio() + "\n" + "Fecha Fin: "+conv.getFechaFin());
	}

}
