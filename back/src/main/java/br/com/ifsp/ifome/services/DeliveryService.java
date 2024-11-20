package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.dto.response.PusherDeliveryOrderResponse;
import br.com.ifsp.ifome.entities.*;
import br.com.ifsp.ifome.exceptions.delivery.DeclineNotAvailableException;
import br.com.ifsp.ifome.exceptions.delivery.DeliveryPersonNotFoundException;
import br.com.ifsp.ifome.exceptions.delivery.NotTheDeliveryPersonResponsibleException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.repositories.OrderInfoDeliveryRepository;
import br.com.ifsp.ifome.repositories.RefuseCustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private static final double RAIO_DA_TERRA_KM = 6371.01; // Raio da Terra em quilômetros
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final RefuseCustomerOrderRepository refuseCustomerOrderRepository;
    private final OrderInfoDeliveryRepository orderInfoDeliveryRepository;
    private final ChoiceDeliveryService choiceDeliveryService;

    public DeliveryService(DeliveryPersonRepository deliveryPersonRepository, CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService, RefuseCustomerOrderRepository refuseCustomerOrderRepository, OrderInfoDeliveryRepository orderInfoDeliveryRepository, ChoiceDeliveryService choiceDeliveryService) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.refuseCustomerOrderRepository = refuseCustomerOrderRepository;
        this.orderInfoDeliveryRepository = orderInfoDeliveryRepository;
        this.choiceDeliveryService = choiceDeliveryService;
    }

    @Scheduled(fixedDelay = 6000)
    private void scheduleDisabledDeliveryPerson() {
        this.deliveryPersonRepository.disabledDeliveryPerson(LocalDateTime.now().minusMinutes(5));
    }

    public List<DeliveryOrderResponse> getOrders(Principal principal) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllByDeliveryPerson(principal.getName());
        customerOrders.forEach(System.err::println);

        return customerOrders.stream().map(DeliveryOrderResponse::new).toList();
    }

    public List<DeliveryPerson> findDeliveryPersonAvailable(Long id) {
        return deliveryPersonRepository.findDeliveryPersonAvailable(id);
    }


    public void updateCoordinates(@Valid CoordinatesRequest coordinatesRequest, Principal principal) {
        Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findByEmail(principal.getName());

        DeliveryPerson deliveryPerson = deliveryPersonOptional
                                        .orElseThrow(
                                            () -> new EntityNotFoundException("Entregador não encontrado")
                                        );

        deliveryPerson.setLatitude(coordinatesRequest.latitude());
        deliveryPerson.setLongitude(coordinatesRequest.longitude());

        deliveryPersonRepository.save(deliveryPerson);
    }

    public void updateOrderStatus(Long orderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        OrderDeliveryStatus orderDeliveryStatus = customerOrder.nextDeliveryStatus();

        boolean status = customerOrder.nextClientStatusByDeliveryStatus();

        customerOrderRepository.save(customerOrder);
        System.err.println("AQUI: " + orderDeliveryStatus);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder);

        if(status) {
            orderStatusUpdateService.updateStatusOrderToClient(customerOrder, customerOrder.getCurrentOrderClientStatus());
        }
    }

    public void previousOrderStatus(Long orderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        if(customerOrder.getOrderInfoDelivery().size() == 1) {
            return;
        }

        OrderInfoDelivery orderDeliveryStatus = customerOrder.previousStatusDelivery();

        orderInfoDeliveryRepository.delete(orderDeliveryStatus);

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder);

//        orderStatusUpdateService.updateStatusOrderToClient(customerOrder, customerOrder.getCurrentOrderClientStatus());
    }

    public void refuseOrder(Long customerOrderId, String justification, Principal principal) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository
                                        .findByEmail(principal.getName())
                                        .orElseThrow(DeliveryPersonNotFoundException::new);

        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow();

        if(customerOrder.getDeliveryPersonId().equals(deliveryPerson.getId())) {
            throw new NotTheDeliveryPersonResponsibleException("Você não é mais o entregador responsavel pelo pedido!");
        }

        var currentOrderDelivery = customerOrder.getOrderInfoDelivery();

        if(currentOrderDelivery.size() >= 4) {
            throw new DeclineNotAvailableException(customerOrder.getCurrentOrderDeliveryStatus().toString());
        }

        customerOrder.setDeliveryPerson(null);

        customerOrderRepository.save(customerOrder);

        OrderInfoDelivery orderDeliveryStatus = customerOrder.previousStatusDelivery();

        orderInfoDeliveryRepository.delete(orderDeliveryStatus);

        var refuseCustomerOrder = new RefuseCustomerOrder(
            customerOrderId, deliveryPerson.getId(), justification
        );

        refuseCustomerOrderRepository.save(refuseCustomerOrder);

        choiceDeliveryService.choiceDeliveryPersonWhenReady(customerOrder);
    }

    public DeliveryPersonResponse getByEmail(String email) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByEmail(email).orElseThrow();
        return new DeliveryPersonResponse(deliveryPerson);
    }

    public void simuleChoice(Long customerOrderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow();

        if(customerOrder.getOrderInfo().size() == 3) {
            customerOrder.setOrderInfo(
                List.of(
                    new OrderInfo(OrderClientStatus.NOVO, LocalDateTime.now(), customerOrder)
                    ,new OrderInfo(OrderClientStatus.EM_PREPARO, LocalDateTime.now(), customerOrder)
                    ,new OrderInfo(OrderClientStatus.PRONTO_PARA_ENTREGA, LocalDateTime.now(), customerOrder)
                )
            );
        }

        this.customerOrderRepository.save(customerOrder);

        choiceDeliveryService.choiceDeliveryPersonWhenReady(customerOrder);
    }

    public Optional<PusherDeliveryOrderResponse> getCustomerOrderId(Principal principal) {
        var customerOrdes = customerOrderRepository.findActiveOrderDeliveryPerson(principal.getName());

        if(customerOrdes.isEmpty()) {
            return Optional.empty();
        }

        CustomerOrder customerOrder = customerOrdes.get(0);

        DeliveryOrderResponse deliveryOrderResponse = new DeliveryOrderResponse(customerOrder);

        return Optional.of(PusherDeliveryOrderResponse.from(customerOrder, deliveryOrderResponse));
    }

    public String setAvailable(Principal principal) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByEmail(principal.getName()).get();

        String available = deliveryPerson.getAvailable();
        deliveryPerson.setAvailable(available.equals("Disponível")? "Indisponível" : "Disponível");
        deliveryPersonRepository.save(deliveryPerson);

        return String.format("Você está %s!", deliveryPerson.getAvailable());
    }

    public void updateStatusOrderToRestaurant(CustomerOrder customerOrder) {
        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder);
    }
}
