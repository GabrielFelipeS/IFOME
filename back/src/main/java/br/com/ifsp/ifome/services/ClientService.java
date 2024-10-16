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
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Client client = this.getClientOrElseThrow(email);

        Cart cart = getCartOrCreateNewCart(email, client);
        return new CartResponse(cart);
    }

    public CartResponse addDishCart(OrderRequest orderRequest, String email) {
        Dish dish = this.getDishOrElseThrow(orderRequest.dishId());
        Client client = this.getClientOrElseThrow(email);

        Cart cart = this.getCartOrCreateNewCart(email, client);
        OrderItem orderItem = new OrderItem(dish, orderRequest.quantity(), cart);
        cart.add(orderItem);

        cart = cartRepository.save(cart);
        return new CartResponse(cart);
    }

    public void updateQuantityOrderItemInCart(OrderRequest orderRequest, String email) {
        Client client = this.getClientOrElseThrow(email);

        this.dishIdExistsOrElseThrow(orderRequest.dishId());

        Cart cart = this.getCartOrCreateNewCart(email, client);
        cart.updateDishInCart(orderRequest.dishId(), orderRequest.quantity());
        this.cartRepository.save(cart);
    }

    public void removeDishInCart(Long dishId, String email) {
        this.dishIdExistsOrElseThrow(dishId);
       
        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);
        Cart cart = optionalCart.orElseThrow(DishNotFoundInCartException::new);
        cart.removeDishBy(dishId);
        this.cartRepository.save(cart);
    }

    private Cart getCartOrCreateNewCart(String email, Client client) {
        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);

        return optionalCart.orElseGet(() -> new Cart(client));
    }

    private Client getClientOrElseThrow(String email) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        return optionalClient.orElseThrow(ClientNotFoundException::new);
    }
    
    private Dish getDishOrElseThrow(Long dishId) {
        Optional<Dish> optionalDish = dishRepository.findDishAvailableById(dishId);
        return optionalDish.orElseThrow(DishNotFoundException::new);
    }

    private void dishIdExistsOrElseThrow(Long dishId) {
        boolean exists = dishRepository.existsById(dishId);

        if(!exists) {
            throw new DishNotFoundException();
        }
    }
}
