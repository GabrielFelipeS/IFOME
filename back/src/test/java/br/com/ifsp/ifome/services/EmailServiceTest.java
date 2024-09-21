package br.com.ifsp.ifome.services;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmailServiceTest {
    private GreenMail greenMail;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.setUser("teste.ifome@gmail.com", "teste");
        greenMail.setUser("test@example.com", "test@example.com");
        greenMail.start();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    @Test
    public void shouldReceiveEmail() throws MessagingException {
        emailService.sendEmail("test@example.com", "Assunto de Teste", "Conteúdo do e-mail");
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        assertThat(receivedMessages.length).isEqualTo(1);

        Message message = receivedMessages[0];

        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("test@example.com");
        assertThat(message.getSubject()).isEqualTo("Assunto de Teste");
    }
}
