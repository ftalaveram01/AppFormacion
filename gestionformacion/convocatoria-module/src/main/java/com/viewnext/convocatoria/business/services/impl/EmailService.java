package com.viewnext.convocatoria.business.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("correogestionformacionvn@gmail.com"); // Tu correo de Gmail
        email.setTo(destinatario);
        email.setSubject(asunto);
        email.setText(mensaje);

        javaMailSender.send(email);
    }
}
