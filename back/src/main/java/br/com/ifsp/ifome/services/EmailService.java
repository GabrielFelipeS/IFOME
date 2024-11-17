package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.events.EmailSentPedidoStatus;
import br.com.ifsp.ifome.events.PedidoStatusChangedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    /**
     * Envia um email de atualização do status para o cliente
     *
     * @param emailSentPedidoStatus Contém os valores que vão ser enviados no email
     * @param statusChangedEvent Evento de atualização do status
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmailStatusChanged(EmailSentPedidoStatus emailSentPedidoStatus, PedidoStatusChangedEvent statusChangedEvent)
        throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailSentPedidoStatus.to());
        helper.setSubject("Seu pedido " + emailSentPedidoStatus.subject());

        Path htmlPath = Paths.get("src/main/resources/emails/atualizacao-pedido.html");
        String htmlContent = new String(Files.readAllBytes(htmlPath))
            .replace("${id_pedido}", statusChangedEvent.getPedidoId().toString())
            .replace("${status_pedido}", emailSentPedidoStatus.subject())
            .replace("${mensagem}", emailSentPedidoStatus.body())
            ;

        helper.setText(htmlContent, true);

        this.sendEmail(message);
    }

    public void sendEmailClientWhenRequestHasTrheeRefused(CustomerOrder customerOrder) {
        this.sendEmail(customerOrder.getEmailClient(), from, "Fudeu maluco, se mora na quebrana, ninguém é louco de ir");
    }
}
