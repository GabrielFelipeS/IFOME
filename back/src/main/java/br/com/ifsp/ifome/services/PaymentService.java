package br.com.ifsp.ifome.services;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
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
}
