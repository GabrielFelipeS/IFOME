package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.OrderItemUpdateRequest;
import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.exceptions.client.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.client.ClientNotFoundException;
import br.com.ifsp.ifome.exceptions.dish.DishNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final DishService dishService;

    public ClientService(ClientRepository clientRepository, DishRepository dishRepository, CartRepository cartRepository, OrderItemRepository orderItemRepository, DishService dishService) {
        this.clientRepository = clientRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.dishService = dishService;
    }

    /**
     * Busca o carrinho do cliente, baseado no email, caso o carrinho não exista cria um carrinho vazio.
     *
     * @param email Email do cliente logado
     * @return Informações do carrinho
     */
    public CartResponse getCart(String email) {
        Client client = this.getClientOrElseThrow(email);

        Cart cart = getCartOrCreateNewCart(email, client);
        return new CartResponse(cart);
    }

    /**
     * Adiciona no carrinho o prato pedido pelo cliente, com id do prato, a quantidade e detalhe.
     *
     * @param orderItemRequest Item a ser adicionado no carrinho, contem o id do prato, quantidade e detalhe.
     * @param email Email do cliente logado.
     * @return Informações do carrinho após a adição.
     * @throws DishNotFoundException Caso não encontre o prato especificado ou esteja indisponível.
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    public CartResponse addDishCart(OrderItemRequest orderItemRequest, String email) {
        Dish dish = dishService.getDishAvailableOrElseThrow(orderItemRequest.dishId());
        Client client = this.getClientOrElseThrow(email);

        Cart cart = this.getCartOrCreateNewCart(email, client);
        OrderItem orderItem = new OrderItem(dish, orderItemRequest.quantity(), orderItemRequest.detail(), cart);

        cart.add(orderItem);

        cart = cartRepository.save(cart);
        return new CartResponse(cart);
    }

    /**
     * Atualiza um item do pedido, buscando pelo id do prato e atualizando a quantidade pela quantidade do orderItemRequest
     *
     * @param orderItemRequest Contém o id do prato a ser buscado e a nova quantidade a ser colocada
     * @param email Email do cliente logado
     * @throws DishNotFoundException Caso não encontre o prato especificado.
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    public void updateQuantityOrderItemInCart(OrderItemUpdateRequest orderItemRequest, String email) {
        Client client = this.getClientOrElseThrow(email);

        dishService.dishIdExistsOrElseThrow(orderItemRequest.dishId());

        Cart cart = this.getCartOrCreateNewCart(email, client);
        cart.updateDishInCart(orderItemRequest.dishId(), orderItemRequest.quantity());
        this.cartRepository.save(cart);
    }

    /**
     * Remove o prato do carrinho do cliente logado baseado no dishId, lança exceções caso não encontre o cliente ou o prato.
     *
     * @param dishId Id do prato a ser removido do carrinho
     * @param email Email do cliente logado
     * @throws DishNotFoundException Caso não encontre o prato ou ele não esteja no carrinho.
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    public void removeDishInCart(Long dishId, String email) {
        dishService.dishIdExistsOrElseThrow(dishId);
        Cart cart = this.getCartOrElseThrow(email);

        OrderItem orderItem = cart.removeDishBy(dishId);;

        orderItemRepository.delete(orderItem);

        this.cartRepository.save(cart);
    }

    /**
     * Remove todos os pratos do carrinho do cleinte
     *
     * @param email Email do cliente logado
     */
    public void clearCart(String email) {
        Cart cart = this.getCartOrElseThrow(email);

        cart.clearCart();

        this.cartRepository.save(cart);
    }

    /**
     * Busca o carrinho do cliente, ou lança uma exception caso não encontre nenhum carrinho ou ele esteja vazio
     *
     * @param email Email do cliente logado
     * @return Carrinho do cliente
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    public Cart getCartNotEmpty(String email) {
        return cartRepository
            .findFirstByClientEmail(email)
            .orElseThrow(CartCannotBeEmptyException::new)
            .cartCannotBeEmpty();
    }

    /**
     * Busca o carrinho do cliente ou cria um carrinho vazio caso não encontre
     *
     * @param email Email do cliente logado
     * @param client Objeto contendo todas as informações do cliente
     * @return Carrinho do cliente
     */
    private Cart getCartOrCreateNewCart(String email, Client client) {
        Optional<Cart> optionalCart = this.cartRepository.findFirstByClientEmail(email);

        return optionalCart.orElseGet(() -> new Cart(client));
    }

    /**
     * Busca o carrinho do cliente, ou lança uma exception caso não encontre nenhum carrinho
     *
     * @param email Email do cliente logado
     * @return Carrinho do cliente
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    private Cart getCartOrElseThrow(String email) {
        return this.cartRepository
            .findFirstByClientEmail(email)
            .orElseThrow(ClientNotFoundException::new);
    }

    /**
     * Busca o cliente baseado no email passado, caso não encontre lança uma exceção
     *
     * @param email Email do cliente logado
     * @return Objeto contendo as informações do cliente
     * @throws ClientNotFoundException Caso não encontre o cliente pelo email passado.
     */
    private Client getClientOrElseThrow(String email) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);
        return optionalClient.orElseThrow(ClientNotFoundException::new);
    }
}
