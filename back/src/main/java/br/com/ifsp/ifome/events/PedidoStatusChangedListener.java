package br.com.ifsp.ifome.events;

import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.mail.javamail.MimeMessageHelper;

@Component
public class PedidoStatusChangedListener  {
    private final EmailService emailService;

    public PedidoStatusChangedListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @EventListener
    public void handlePedidoStatusChanged(PedidoStatusChangedEvent event) throws MessagingException {
        System.out.println("Pedido ID: " + event.getPedidoId() + " teve seu status alterado para: " + event.getNewStatus());
        String nomeCliente = event.getCustomerOrder().getCart().getClient().getName();
        String restaurantName = event.getCustomerOrder().getRestaurant().getNameRestaurant();
        String email = event.getCustomerOrder().getCart().getClient().getEmail();

        Map<OrderStatus, EmailSentPedidoStatus> hashEvent = Map.of(
            OrderStatus.NOVO, new EmailSentPedidoStatus(email, "Ta faltando mensagem para novo", "mensagem boa"),
            OrderStatus.ACEITO, new EmailSentPedidoStatus(email, "Ta faltando mensagem para novo", "mensagem boa"),
            OrderStatus.EM_PREPARO, new EmailSentPedidoStatus(email, "Seu pedido está sendo praparado!",
                String.format("Olá %s, \nSeu pedido no restaurante %s está sendo preparado no momento. Estaremos prontos para enviá-lo em breve. Fique atento!", nomeCliente, restaurantName)
                ),
            OrderStatus.PRONTO_PARA_ENTREGA, new EmailSentPedidoStatus(email, "Seu pedido está pronto para ser entregue!",
                String.format("Olá %s, \nSeu pedido já está pronto! Estamos apenas aguardando o entregador para iniciar a entrega. Seu pedido estará com você em breve!", nomeCliente)),
            OrderStatus.SAIU_PARA_ENTREGA, new EmailSentPedidoStatus(email, "Seu pedido está a caminho!", String.format("Olá %s, \nO entregador já está a caminho com seu pedido. Acompanhe sua entrega em tempo real pelo sistema. Aproveite!", nomeCliente)),
            OrderStatus.CONCLUIDO, new EmailSentPedidoStatus(email, "Seu pedido foi entregue!", String.format("Olá %s, \nEsperamos que você aproveite sua refeição! Seu pedido foi entregue com sucesso. Agradecemos por usar nosso serviço e esperamos vê-lo em breve!", nomeCliente))
            );

        EmailSentPedidoStatus emailSentPedidoStatus = hashEvent.get(event.getNewStatus());
        System.err.println(emailSentPedidoStatus.to());
        System.err.println(emailSentPedidoStatus.subject());
        System.err.println(emailSentPedidoStatus.body());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailSentPedidoStatus.to());
        helper.setSubject(emailSentPedidoStatus.subject());

        // Corpo do e-mail em HTML, incluindo a imagem embutida
//        String htmlContent = """
//        <html>
//        <body>
//         <table>
//                <tr role='presentation' width='100%' cellspacing='0' cellpadding='0' border='0'>
//                    <td align='center' style='padding: 10px 0;'>
//                        <img src='cid:rodapeImage' alt='Imagem centralizada'
//                             style='display: block; width: 20px; height: 20px;'/>
//                    </td>
//                </tr>
//                <tr>
//                    <td>
//                    %s
//                    </td>
//                </tr>
//        </table>
//            </body>
//        </html>""";
//        System.err.println("AQUI");

        String htmlContent =
            " <html><body>" +
                "<table role='presentation' width='100%' cellspacing='0' cellpadding='0' border='0'>"
                + "    <tr>"
                + "        <td align='center' style='padding: 10px 0;'>"
                + "            <img src='cid:rodapeImage' alt='Imagem centralizada'"
                + "                 style='display: block; width: 200px; height: 200px;'/>"
                + "        </td>"
                + "    </tr>"
                + "    <tr>"
                + "        <td>"+emailSentPedidoStatus.body() +"</td>"
                + "    </tr>"
                + "</table>" +
                "</body> </html>";

        helper.setText(htmlContent, true);

        ClassPathResource image = new ClassPathResource("/static/images/logo_para_email.png");
        helper.addInline("rodapeImage", image);

        emailService.sendEmail(message);
    }
}
