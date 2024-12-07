package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.PaymentConfirmRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public static final String currency = "brl";

    private final CustomerOrderService customerOrderService;

    public PaymentService(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    public String getClientSecret(Long orderId, String email) {
        var  customerOrder = customerOrderService.findById(orderId, email);

        Long ammountInCent = customerOrder.totalPriceInCent();

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(ammountInCent)
                .setCurrency(currency)
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return paymentIntent.getClientSecret(); // Retorna o client secret
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    public PaymentIntent confirmPayment(PaymentConfirmRequest paymentConfirmRequest, String email) {
        try {
            String paymentMethodId = paymentConfirmRequest.paymentMethodId();

            var  customerOrder = customerOrderService.findById(paymentConfirmRequest.orderId(), email);

            Long ammountInCent = customerOrder.totalPriceInCent();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(ammountInCent) // Ex.: 5000 (para $50.00)
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true) // Confirma o pagamento diretamente
                .build();

            return PaymentIntent.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar pagamento");
        }
    }
}
