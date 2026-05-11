package com.ProjetoExtensao.CoinEdu.service;


import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public String gerarCodigo() {
        return String.format("%06d" , new SecureRandom().nextInt(999999));
    }

    public void enviarCodigo(String destinatario , String codigo , String tipo) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(destinatario);

        if (tipo.equals("cadastro")) {
            msg.setSubject("CoinEdu - Confirme seu cadastro");
            msg.setText("""
                    Bem vindo ao CoinEdu $
                    
                    seu código de verificação é:
                    
                    %s
                    
                    O código expira em 15 minutos.
                    se não foi você, apena ignore este e-mail.

                    """.formatted(codigo));
        }
        else {
            msg.setSubject("CoinEdu - Código de acesso");
            msg.setText("""
                    Aqui está seu código de acesso ao CoinEdu:
                    
                    %s
                    
                    O codigo expira em 15 minutos.
                    Se não foi você , ignore este e-mail.
                    """.formatted(codigo));
        }
        mailSender.send(msg);
    }



}
