package com.viewnext.convocatoria.business.services.impl;


import java.util.EnumSet;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.viewnext.convocatoria.business.services.ConvocatoriaScheduler;
import com.viewnext.convocatoria.business.services.ConvocatoriaServices;

import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaEnum;

import com.viewnext.core.business.model.Course;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.repositories.ConvocatoriaRepository;
import com.viewnext.core.repositories.CursoRepository;
import com.viewnext.core.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ConvocatoriaServicesImpl implements ConvocatoriaServices {
	
	private ConvocatoriaRepository convocatoriaRepository;
	
	private CursoRepository cursoRepository;
	
	private ConvocatoriaScheduler convocatoriaScheduler;
	
	private UsuarioRepository usuarioRepository;
	
	public ConvocatoriaServicesImpl(ConvocatoriaRepository convocatoriaRepository,
			ConvocatoriaScheduler convocatoriaScheduler, CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
		this.convocatoriaRepository = convocatoriaRepository;
		this.convocatoriaScheduler = convocatoriaScheduler;
		this.cursoRepository = cursoRepository;
		this.usuarioRepository = usuarioRepository;
	}

    @Transactional
	@Override
	public Convocatoria create(Long idAdmin, ConvocatoriaRequest request) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No tienes permisos para realizar esta accion");
		
		if(request.getFechaFin().before(request.getFechaInicio()))
			throw new IllegalStateException("La fecha de inicio no puede ser despues de la de fin.");
		
		if(request.getFechaInicio().before(new Date()))
			throw new IllegalStateException("La fecha de inicio no puede ser antes de la actual.");
		
		if(!cursoRepository.existsById(request.getIdCurso()))
			throw new IllegalStateException("No existe el curso de la convocatoria");
		
		Course curso = cursoRepository.findById(request.getIdCurso()).get();
		
		if(!curso.getHabilitado())
			throw new IllegalStateException("El curso está deshabilitado");
		
		if(curso.getUsuarios().size()<10)
			throw new IllegalStateException("No hay suficientes alumnos inscritos al curso.");
		
		Convocatoria conv = new Convocatoria();
		conv.setFechaInicio(request.getFechaInicio());
		conv.setFechaFin(request.getFechaFin());
		conv.setCurso(curso);
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		conv.setUsuarios(new ArrayList<Usuario>());
		
		Convocatoria guardada = convocatoriaRepository.save(conv);
		convocatoriaScheduler.programarTarea(guardada, true, false);
		
		
		//enviarCorreo(guardada);
		
		return guardada;
	}

	@Override
	public List<Convocatoria> getAll(Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No eres administrador");
		
		return convocatoriaRepository.findAll();
	}

	@Override
	public List<Convocatoria> getActivas() {
		
		EnumSet<ConvocatoriaEnum> estadosActivos = EnumSet.of(
	            ConvocatoriaEnum.CONVOCADA,
	            ConvocatoriaEnum.EN_CURSO,
	            ConvocatoriaEnum.EN_PREPARACION
	        );
		
		return convocatoriaRepository.findAll().stream()
											   .filter(c ->estadosActivos.contains(c.getEstado()))
											   .toList();
	}

    @Transactional
	@Override
	public void update(Long id, Long idAdmin, UpdateRequest request) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No tienes permisos para realizar esta accion");
		
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
		conv.setUsuarios(new ArrayList<Usuario>());
		conv.setEstado(ConvocatoriaEnum.EN_PREPARACION);
		
		convocatoriaRepository.save(conv);
		
		convocatoriaScheduler.programarTarea(conv, true, false);
		
		//this.enviarCorreo(conv);
	}

    @Transactional
	@Override
	public void delete(Long id, Long idAdmin) {
		
		if(!isAdmin(idAdmin))
			throw new IllegalStateException("No tienes permisos para realizar esta accion");
		
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
		
        String host = "smtp.gmail.com"; //servidor SMTP
        final String user = "appformacion3@gmail.com"; //tu correo
        final String password = "gestionformacion"; //tu contraseña

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        
        try {
			
        	MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.get().getEmail()));
            message.setSubject("ENHORABUENA, AQUI TIENES TU CERTIFICADO ");
            message.setText("Has completado el curso de "+convocatoria.get().getCurso().getNombre());
            message.setText("Fecha Inicio: "+convocatoria.get().getFechaInicio());
            message.setText("Fecha Fin: "+convocatoria.get().getFechaFin());
            
            Transport.send(message);
            
            System.out.println("CERTIFICADO ENVIADO CORRECTAMENTEs");
        	
		} catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("ERROR AL ENVIAR CERTIFICADO");
        }
		        
		
	}

    @Transactional
	@Override
	public void inscribirUsuario(Long idConvocatoria, Long idUsuario) {
    	
    	System.out.println(idConvocatoria);
		
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
	
	private boolean isAdmin(Long idAdmin) {
		if(!usuarioRepository.existsById(idAdmin))
			throw new IllegalStateException("No existe el usuario admin");
		return usuarioRepository.isAdmin(idAdmin);
	}
	
	private void enviarCorreo(Convocatoria convocatoria) {
		
		//email = appformacion3@gmail.com
		//password = wasx goao szjt uiwd
		
        String host = "smtp.gmail.com"; //servidor SMTP
        final String user = "appformacion3@gmail.com"; //tu correo
        final String password = "gestionformacion"; //tu contraseña

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        

        try {
            for (Usuario usuario: convocatoria.getCurso().getUsuarios()) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
                message.setSubject("Convocatoria para el curso "+convocatoria.getCurso().getNombre());
                message.setText("Acepta la convocatoria en el siguiente apartado.");

                System.out.println("AQUI PETA !");
                Transport.send(message);
                
            }
            System.out.println("Correos enviados exitosamente");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("ERROR AL ENVIAR CORREO");
        }
	}
}
