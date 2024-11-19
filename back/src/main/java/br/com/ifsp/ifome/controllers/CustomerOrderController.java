package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocGetCustomerOrders;
import br.com.ifsp.ifome.docs.DocGetRestaurantOrders;
import br.com.ifsp.ifome.docs.DocUpdateCustomerOrderStatus;
import br.com.ifsp.ifome.docs.DocsCreateCustomerOrder;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.services.CustomerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping
    @DocsCreateCustomerOrder
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        CustomerOrderResponse customerOrderRequest = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", customerOrderRequest, "Pedido enviado! Aguarde confimação!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/customerOrders")
    @DocGetCustomerOrders
    public ResponseEntity<List<CustomerOrderResponse>> getAllCustomerOrders(Principal principal) {
        String customerEmail = principal.getName();

        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByCustomer(customerEmail);
        return ResponseEntity.ok(orders);
    }

    @DocGetRestaurantOrders
    @GetMapping("/restaurantOrders")
    public ResponseEntity<List<CustomerOrderResponse>> getAllRestaurantOrders(Principal principal) {
        String  restaurantEmail = principal.getName();

        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByRestaurant(restaurantEmail);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/updateStatus/{customerOrderId}")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long customerOrderId, Principal principal) {
        customerOrderService.updateOrderStatus(customerOrderId, principal.getName());

        ApiResponse response = new ApiResponse("success", null, "Status atualizado com sucesso!");
        return ResponseEntity.ok(response);
    }

}
