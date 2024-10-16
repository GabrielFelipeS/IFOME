package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.exceptions.ClientNotFoundException;
import br.com.ifsp.ifome.exceptions.DishNotFoundException;
import br.com.ifsp.ifome.exceptions.DishNotFoundInCartException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO refatorar código
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final DishRepository dishRepository;
    private final CartRepository cartRepository;

    public ClientService(ClientRepository clientRepository, DishRepository dishRepository, CartRepository cartRepository) {
        this.clientRepository = clientRepository;
        this.dishRepository = dishRepository;
        this.cartRepository = cartRepository;
    }

    public CartResponse getCart(String email) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        Client client = optionalClient.orElseThrow(ClientNotFoundException::new);

        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);
        Cart cart = optionalCart.orElseGet(() -> new Cart(client));
        return new CartResponse(cart);
    }

    public CartResponse addDishCart(OrderRequest orderRequest, String email) {
        Optional<Dish> optionalDish = dishRepository.findDishAvailableById(orderRequest.dishId());
        Optional<Client> optionalClient = clientRepository.findByEmail(email);

        Dish dish = optionalDish.orElseThrow(DishNotFoundException::new);
        Client client = optionalClient.orElseThrow(ClientNotFoundException::new);

        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);
        Cart cart = optionalCart.orElseGet(() -> new Cart(client));

        OrderItem orderItem = new OrderItem(dish, orderRequest.quantity(), cart);
        cart.add(orderItem);

        cart = cartRepository.save(cart);
        return new CartResponse(cart);
    }

    public void updateQuantityOrderItemInCart(OrderRequest orderRequest, String email) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        Client client = optionalClient.orElseThrow(ClientNotFoundException::new);

        Optional<Dish> optionalDish = dishRepository.findDishAvailableById(orderRequest.dishId());
        Dish dish = optionalDish.orElseThrow(DishNotFoundException::new);

        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);

        Cart cart = optionalCart.orElseGet(() -> new Cart(client));
        cart.updateDishInCart(orderRequest.dishId(), orderRequest.quantity());
        this.cartRepository.save(cart);
    }


    public void removeDishInCart(Long dishId, String email) {
        System.err.println(dishId);
        System.err.println("AQUI 1");
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        Client client = optionalClient.orElseThrow(ClientNotFoundException::new);
        System.err.println("AQUI 2");
        Optional<Dish> optionalDish = dishRepository.findDishAvailableById(dishId);
        Dish dish = optionalDish.orElseThrow(DishNotFoundException::new);
        System.err.println("AQUI 3");
        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);
        System.err.println("AQUI 4");
        Cart cart = optionalCart.orElseThrow(DishNotFoundInCartException::new);
        System.err.println("AQUI 5");
        cart.removeDishBy(dishId);
        System.err.println("AQUI 6");
        this.cartRepository.save(cart);
    }
}
