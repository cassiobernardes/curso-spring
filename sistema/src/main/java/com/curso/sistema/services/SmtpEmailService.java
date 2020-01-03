package com.curso.sistema.services;

import com.curso.sistema.models.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmtpEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email-sender}")
    private String emailSander;

    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    public void sendEmail(Pedido pedido) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(pedido.getCliente().getEmail());
        simpleMailMessage.setFrom(emailSander);
        simpleMailMessage.setSubject("Pedido Confirmado! Código: " + pedido.getId());
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(pedido.toString());
        LOG.info("Enviando e-mail...");
        javaMailSender.send(simpleMailMessage);
        LOG.info("E-mail enviado.");
    }


}
