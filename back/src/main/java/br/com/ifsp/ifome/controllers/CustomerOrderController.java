package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.services.CustomerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        CustomerOrderRequest customerOrderRequest = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", customerOrderRequest, "Pedido enviado! Aguarde confimação!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    SseEmitter emitter;

    @GetMapping("/status")
    public SseEmitter getStatusCustomerOrder() throws IOException {
        emitter = new SseEmitter(Long.MAX_VALUE);

        Long clienteId = 1L;
        System.err.println("AQUI");
        emitter.send("Status atual: Pedido recebido");

        emitter.onTimeout(() -> emitter.onTimeout(() -> System.out.println("Parou")));
        emitter.onError((e) -> emitter.onError(System.err::println));
        emitter.onCompletion(() -> emitter.complete());

        return emitter;
    }

    @GetMapping("/teste")
    public void teste() throws IOException {
        emitter.send("Teste " + LocalDateTime.now().getSecond());
    }


}
