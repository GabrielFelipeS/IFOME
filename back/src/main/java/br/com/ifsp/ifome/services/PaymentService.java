package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.LoggingAspect;
import br.com.ifsp.ifome.dto.request.PaymentConfirmRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.stripe.model.Event;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    public static final String currency = "brl";
    private final CustomerOrderService customerOrderService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    public PaymentService(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    public String getClientSecret(Long orderId, String email) {
        var customerOrder = customerOrderService.findById(orderId, email);

        Long ammountInCent = customerOrder.totalPriceInCent();

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(ammountInCent)
                .setCurrency(currency)
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            String clientSecret = paymentIntent.getClientSecret();

            customerOrder.setClientSecret(clientSecret);

            return clientSecret;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    public String webHook(HttpServletRequest request, String payload) {
        String sigHeader = request.getHeader("Stripe-Signature");

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            return "Webhook signature verification failed";
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (paymentIntent != null) {
                    logger.info("Pagamento aprovado para o cliente: " + paymentIntent.getCustomer());
                    logger.info("Montante pago: " + paymentIntent.getAmountReceived() / 100.0 + " " + paymentIntent.getCurrency());
                }
                break;

            case "payment_intent.payment_failed":
                logger.warn("Pagamento falhou: " + event.getDataObjectDeserializer().getObject().orElse(null));
                break;

            default:
                logger.info("Evento não tratado: " + event.getType());
        }

        return "Webhook processado com sucesso";
    }

    public PaymentIntent confirmPayment(PaymentConfirmRequest paymentConfirmRequest, String email) {
        try {
            String paymentMethodId = paymentConfirmRequest.paymentMethodId();

            var  customerOrder = customerOrderService.findById(paymentConfirmRequest.orderId(), email);

            Long ammountInCent = customerOrder.totalPriceInCent();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(ammountInCent)
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .build();

            return PaymentIntent.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar pagamento");
        }
    }
}
