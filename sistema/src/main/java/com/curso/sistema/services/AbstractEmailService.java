package com.curso.sistema.services;

import com.curso.sistema.models.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public class AbstractEmailService implements EmailService {

    @Value("${email-sender}")
    private String emailSander;

    @Override
    public void sendOrderConfirmationEmail(Pedido pedido) {
        SimpleMailMessage simpleMailMessage =  prepareSimpleMailMessageFromPedido(pedido);
        sendEmail(simpleMailMessage);
    }

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {

    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(pedido.getCliente().getEmail());
        simpleMailMessage.setFrom(emailSander);
        simpleMailMessage.setSubject("Pedido Confirmado! Código: " + pedido.getId());
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(pedido.toString());
        return simpleMailMessage;
    }



}
