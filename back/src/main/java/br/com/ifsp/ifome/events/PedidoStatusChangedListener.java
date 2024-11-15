package br.com.ifsp.ifome.events;

import br.com.ifsp.ifome.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PedidoStatusChangedListener  {
    private final EmailService emailService;

    public PedidoStatusChangedListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handlePedidoStatusChanged(PedidoStatusChangedEvent event) throws MessagingException, IOException {
        EmailSentPedidoStatus emailSentPedidoStatus = this.getEvent(event);

        emailService.sendEmailStatusChanged(emailSentPedidoStatus, event);
    }

    // TODO Deve ter um jeito mais bonito de fazer isso
    private EmailSentPedidoStatus getEvent(PedidoStatusChangedEvent event) {
        String nomeCliente = event.getClientName();
        String restaurantName = event.getRestaurantName();
        String email = event.getCustomerOrder().getCart().getClient().getEmail();

        return switch (event.getNewStatus()) {
            case NOVO -> new EmailSentPedidoStatus(
                email,
                "Ta faltando mensagem para novo",
                "mensagem boa");

            case EM_PREPARO -> new EmailSentPedidoStatus(
                email,
                "está sendo praparado!",
                String.format("Olá %s, \nSeu pedido no restaurante %s está sendo preparado no momento. Estaremos prontos para enviá-lo em breve. Fique atento!", nomeCliente, restaurantName));

            case PRONTO_PARA_ENTREGA -> new EmailSentPedidoStatus(
                email,
                "está pronto para ser entregue!",
                String.format("Olá %s, \nSeu pedido já está pronto! Estamos apenas aguardando o entregador para iniciar a entrega. Seu pedido estará com você em breve!", nomeCliente)) ;

            case SAIU_PARA_ENTREGA -> new EmailSentPedidoStatus(
                email,
                "está a caminho!",
                String.format("Olá %s, \nO entregador já está a caminho com seu pedido. Acompanhe sua entrega em tempo real pelo sistema. Aproveite!", nomeCliente));

            case CONCLUIDO -> new EmailSentPedidoStatus(
                email,
                "foi entregue!",
                String.format("Olá %s, \nEsperamos que você aproveite sua refeição! Seu pedido foi entregue com sucesso. Agradecemos por usar nosso serviço e esperamos vê-lo em breve!", nomeCliente));
        };
    }
}
