package com.viewnext.convocatoria.business.services.impl;

import java.util.List;
import java.util.Properties;

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
import com.viewnext.convocatoria.integration.repositories.ConvocatoriaRepository;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.Usuario;

@Service
public class ConvocatoriaServicesImpl implements ConvocatoriaServices {
	
	private ConvocatoriaRepository convocatoriaRepository;
	
	private ConvocatoriaScheduler convocatoriaScheduler;
	
	public ConvocatoriaServicesImpl(ConvocatoriaRepository convocatoriaRepository,
			ConvocatoriaScheduler convocatoriaScheduler) {
		this.convocatoriaRepository = convocatoriaRepository;
		this.convocatoriaScheduler = convocatoriaScheduler;
	}

	@Override
	public Convocatoria create(Long idAdmin, ConvocatoriaRequest request) {
		// TODO Auto-generated method stub
		
		
		enviarCorreo(null);
		
		return null;
	}

	@Override
	public List<Convocatoria> getAll(Long idAdmin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convocatoria> getActivas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Long id, Long idAdmin, UpdateRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id, Long idAdmin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Convocatoria> getFromUsuario(Long idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generarCertificado(Long idConvocatoria, Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inscribirUsuario(Long idConvocatoria, Long idUsuario) {
		// TODO Auto-generated method stub
		
	}
	
	private void enviarCorreo(Convocatoria convocatoria) {
		
		//email = appformacion3@gmail.com
		//password = pecera77
		
        String host = "smtp.gmail.com"; //servidor SMTP
        final String user = "appformacion3@gmail.com"; //tu correo
        final String password = "pecera77"; //tu contraseña

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
            for (Usuario usuario: convocatoria.getUsuarios()) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
                message.setSubject("Convocatoria para el curso "+convocatoria.getCurso().getNombre());
                message.setText("Acepta la convocatoria en el siguiente apartado.");

                Transport.send(message);
            }
            System.out.println("Correos enviados exitosamente");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("ERROR AL ENVIAR CORREO");
        }
	}

}
