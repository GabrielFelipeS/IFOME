package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.events.PedidoStatusChangedEvent;
import br.com.ifsp.ifome.exceptions.client.CustomerNotFoundInCartException;
import br.com.ifsp.ifome.exceptions.restaurant.OrderNotFromRestaurantException;
import br.com.ifsp.ifome.exceptions.restaurant.RestaurantIsCloseException;
import br.com.ifsp.ifome.exceptions.restaurant.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final RestaurantService restaurantService;
    private final ClientService clientService;
    private final ChoiceDeliveryService choiceDeliveryService;
    private final ApplicationEventPublisher eventPublisherService;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository,
                                OrderStatusUpdateService orderStatusUpdateService,
                                RestaurantService restaurantService,
                                ClientService clientService, ChoiceDeliveryService choiceDeliveryService, ApplicationEventPublisher eventPublisherService) {
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.restaurantService = restaurantService;
        this.clientService = clientService;
        this.choiceDeliveryService = choiceDeliveryService;
        this.eventPublisherService = eventPublisherService;
    }

    /**
     * Cria um pedido baseado no carrinho não vazio do cliente
     *
     * @param principal Indivíduo que está logado
     * @return Informações do pedido criado
     * @throws RestaurantIsCloseException Quando o restaurante está fechado
     */
    // TODO mover o essa pegada de pegar o restaurante para o clientService
    public CustomerOrderResponse createOrder(Principal principal) {
        Cart cart = clientService.getCartNotEmpty(principal.getName());

        Restaurant restaurant = restaurantService.findById(cart.getIdRestaurant());

        if(restaurant.isClose()) {
            throw new RestaurantIsCloseException("O restaurante está fechado, não pode aceitar pedidos!");
        }

        CustomerOrder customerOrder = new CustomerOrder(cart, restaurant);

        customerOrderRepository.save(customerOrder);

        return CustomerOrderResponse.from(customerOrder);
    }

    /**
     * Pegar todos os pedidos do cliente
     *
     * @param customerEmail Email do cliente
     * @return Lista de pedidos do cliente
     */
    public List<CustomerOrderResponse> getAllOrdersByCustomer(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        return orders.stream()
                .map(CustomerOrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos os pedidos do restaurante
     *
     * @param restaurantEmail Email do restaurante
     * @return Lista de pedidos
     * @throws RestaurantNotFoundException Caso não encontre o restaurante
     */
    public List<CustomerOrderResponse> getAllOrdersByRestaurant(String restaurantEmail) {
        Restaurant restaurant = restaurantService.findByEmail(restaurantEmail);

        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(restaurant.getId());

        if(orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(CustomerOrderResponse::from) // Assumindo que você tem um construtor adequado
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o status do pedido de um restaurante para o próximo status. Caso o status do pedido vá para {@code PRONTO_PARA_ENTREGAR} inicia-se o processo de busca de um entregador
     *
     * @param orderId Id do pedido
     * @param email Email do restaurante
     * @throws CustomerNotFoundInCartException Caso não encontre o pedido
     * @throws OrderNotFromRestaurantException Caso o pedido seja de outro restaurante
     */
    public void updateOrderStatus(Long orderId, String email) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new CustomerNotFoundInCartException("Pedido não encontrado com ID: " + orderId));

        if(customerOrder.getRestaurantEmailDoesNotEquals(email)) {
            throw new OrderNotFromRestaurantException("O pedido não pertence ao restaurante logado");
        }

        OrderClientStatus orderClientStatus = customerOrder.nextStatus();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToClient(customerOrder, orderClientStatus);
        System.err.println("EXECUTOU");
        choiceDeliveryService.choiceDeliveryPersonWhenReady(customerOrder);

// TODO fazer os testes mockarem isso, e executar testes onde verifica o envio do email
        eventPublisherService.publishEvent(new PedidoStatusChangedEvent(customerOrder.getId(), orderClientStatus, customerOrder));
    }

    /**
     * Atualiza o status do pedido de um restaurante para o status anterior.
     *
     * @param orderId Id do pedido
     * @param email Email do restaurante
     * @throws CustomerNotFoundInCartException Caso não encontre o pedido
     * @throws OrderNotFromRestaurantException Caso o pedido seja de outro restaurante
     */
    public void previousOrderStatus(Long orderId, String email) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        if(customerOrder.getRestaurantEmailDoesNotEquals(email)) {
            throw new OrderNotFromRestaurantException("O pedido não pertence ao restaurante logado");
        }

        OrderClientStatus orderClientStatus = customerOrder.previousStatus();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToClient(customerOrder, orderClientStatus);
    }
}
