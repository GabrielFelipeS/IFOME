package br.com.ifsp.ifome.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * Envia um email usando a conta de {@code  spring.mail.username}
     *
     * @param to Para quem enviar o email
     * @param subject Assuno do email
     * @param body O corpo do email
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);

        mailSender.send(message);
    }

    /**
     * Envia um email usando a conta de {@code  spring.mail.username}
     *
     * @param mimeMessage Informações do email, incluindo destino, assunto e corpo
     */
    public void sendEmail(MimeMessage mimeMessage) throws MessagingException {
        mimeMessage.setFrom(from);
        mailSender.send(mimeMessage);
    }
}
