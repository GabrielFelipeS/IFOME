package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.exceptions.ClientNotFoundException;
import br.com.ifsp.ifome.exceptions.DishNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final DishRepository dishRepository;
    private final ClientRepository clientRepository;
    private CartRepository cartRepository;

    public ClientController(CartRepository cartRepository, DishRepository dishRepository, ClientRepository clientRepository) {
        this.cartRepository = cartRepository;
        this.dishRepository = dishRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public Cart addDishInCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        Optional<Dish> optionalDish = dishRepository.findById(orderRequest.dishId());
        Optional<Client> optionalClient = clientRepository.findByEmail(principal.getName());

        Dish dish = optionalDish.orElseThrow(DishNotFoundException::new);
        Client client = optionalClient.orElseThrow(ClientNotFoundException::new);

        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(principal.getName());
        Cart cart = optionalCart.orElseGet(() -> new Cart(client));

        OrderItem orderItem = new OrderItem(dish, orderRequest.quantity(), cart);
        cart.add(orderItem);

        System.err.println(optionalCart.isPresent());
        System.err.println(cart);

        cart = cartRepository.save(cart);
        return cart;
    }
}
