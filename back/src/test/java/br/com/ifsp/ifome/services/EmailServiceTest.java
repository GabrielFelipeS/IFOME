package br.com.ifsp.ifome.services;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailServiceTest {
    private  GreenMail greenMail;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.start();
    }

    @Test
    public void shouldReceiveEmail() {
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        assertThat(receivedMessages.length).isEqualTo(1);
    }
}
