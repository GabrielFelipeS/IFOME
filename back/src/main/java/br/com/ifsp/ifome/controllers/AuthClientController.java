package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.aspect.Login;
import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.docs.DocsClientLogin;
import br.com.ifsp.ifome.docs.DocsCreateClient;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.ForgotPasswordRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.services.AuthClientService;
import br.com.ifsp.ifome.services.PaymentService;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth/client")
public class AuthClientController {
    private final AuthClientService authClientService;
    private final DishRepository dishRepository;

    public AuthClientController(AuthClientService authClientService, DishRepository dishRepository) {
        this.authClientService = authClientService;
        this.dishRepository = dishRepository;
    }

    @SensitiveData
    @PostMapping
    @DocsCreateClient
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ClientRequest clientRequest , UriComponentsBuilder ucb) throws MethodArgumentNotValidException {
        ClientResponse clientResponse = authClientService.create(clientRequest);

        URI locationOfNewClient = ucb
            .path("client/{id}")
            .buildAndExpand(clientResponse.id())
            .toUri();

        ApiResponse response = new ApiResponse("success", clientResponse, "Cliente criado com sucesso");
        return ResponseEntity.created(locationOfNewClient).body(response);
    }

    @Login
    @SensitiveData
    @DocsClientLogin
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authClientService.login(loginRequest);

        ApiResponse apiResponse = new ApiResponse("success", loginResponse, "Cliente logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    // TODO Terminar isso
    @PostMapping("/forgot_password")
    public void forgotPassword(HttpServletRequest request, @RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        System.err.println(request.getServerName());
        System.out.println(forgotPasswordRequest.email());
        authClientService.forgotPassword(request, forgotPasswordRequest.email());
    }

    @GetMapping("/change_password")
    public void changePassword(@RequestParam("token") String token) {
        System.err.println(token);
    }

//    @GetMapping("/generate")
//    public void generate() throws Exception {
//        var dishes = dishRepository.findAll();
//
//        for(Dish dish : dishes) {
//            var priceId = this.createPrice(dish.getName(), dish.getPrice());
//            System.err.printf("Id: %d Nome: %s Price id: %s", dish.getId(), dish.getName(), priceId);
//        }
//    }
//
//    private String createPrice(String productName, Double amount) throws Exception {
//        ProductCreateParams productParams = ProductCreateParams.builder()
//            .setName(productName)
//            .build();
//
//        Product product = Product.create(productParams);
//
//        PriceCreateParams priceParams = PriceCreateParams.builder()
//            .setUnitAmount(Double.valueOf(amount * 100).longValue())
//            .setCurrency(PaymentService.currency)
//            .setProduct(product.getId())
//            .build();
//
//        Price price = Price.create(priceParams);
//
//        return price.getId();
//    }

}
