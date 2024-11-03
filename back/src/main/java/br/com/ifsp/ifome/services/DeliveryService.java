package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.entities.*;
import br.com.ifsp.ifome.exceptions.DeliveryPersontNotFoundException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.repositories.RefuseCustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
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

    public DeliveryService(DeliveryPersonRepository deliveryPersonRepository, CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService, RefuseCustomerOrderRepository refuseCustomerOrderRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.refuseCustomerOrderRepository = refuseCustomerOrderRepository;
    }

    public List<DeliveryOrderResponse> getOrders(Principal principal) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllByDeliveryPerson(principal.getName());
        customerOrders.forEach(System.err::println);

        return customerOrders.stream().map(DeliveryOrderResponse::new).toList();
    }

    @Async  // TODO melhorar forma de busca
    public void choiceDeliveryPersonWhenReady(CustomerOrder customerOrder) {
        boolean isNotReady = !customerOrder.getCurrentOrderClientStatus().equals(OrderClientStatus.PRONTO_PARA_ENTREGA);
        if(isNotReady) return;

        System.err.println(customerOrder.getRestaurantAddress());

        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findDeliveryPersonAvailable();

        deliveryPersons.stream().forEach(System.err::println);
        Address addressRestaurant = customerOrder.getRestaurantAddress();

        double minDistance = Double.MAX_VALUE;

        System.err.println("AQUI 1");
        DeliveryPerson deliveryPersonChoice = null;
        System.err.println("AQUI 2");

        for(DeliveryPerson deliveryPerson : deliveryPersons) {
            double distance = calculateDistance(addressRestaurant, deliveryPerson.getLatitude(), deliveryPerson.getLongitude());
            System.err.println(distance);
            if(distance < minDistance) {
                minDistance = distance;
                deliveryPersonChoice = deliveryPerson;
            }
        }

        if(deliveryPersonChoice == null) {
            // TODO criar uma classe thread que emcapsula isso, para também usar um limitador
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(10000);
//                    this.choiceDeliveryPersonWhenReady(customerOrder);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            thread.start();
            return;
        }

        customerOrder.setDeliveryPerson(deliveryPersonChoice);
        System.err.println(minDistance);
        double preciseDelivery = minDistance * 1;

        customerOrder.setDeliveryCost(preciseDelivery);
        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder, OrderDeliveryStatus.NOVO);
    }

    private double calculateDistance(Address restaurantAddress, String latitudeDeliveryPerson, String longitudeDeliveryPerson) {
        double latRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLatitude()));
        double lonRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLongitude()));
        double latDeliveryPerson = Math.toRadians(Double.parseDouble(latitudeDeliveryPerson));
        double lonDeliveryPerson = Math.toRadians(Double.parseDouble(longitudeDeliveryPerson));

        double diffLatitude = latDeliveryPerson - latRestaurant;
        double diffLongitude = lonDeliveryPerson - lonRestaurant;

        System.err.println("Latitude");
        System.err.println(latRestaurant);
        System.err.println(latDeliveryPerson);

        System.err.println("Longitude");
        System.err.println(lonRestaurant);
        System.err.println(lonDeliveryPerson);

        System.err.println("Diferença");
        System.err.println(diffLatitude);
        System.err.println(diffLongitude);

        double diffAngular = Math.pow(Math.sin(diffLatitude / 2), 2)
            + Math.cos(latRestaurant) * Math.cos(latDeliveryPerson) * Math.pow(Math.sin(diffLongitude / 2), 2);
        double anguloCentral = 2 * Math.atan2(Math.sqrt(diffAngular), Math.sqrt(1 - diffAngular));

        return RAIO_DA_TERRA_KM * anguloCentral;
    }

    public void updateCoordinates(CoordinatesRequest coordinatesRequest, Principal principal) {
        Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findByEmail(principal.getName());

        DeliveryPerson deliveryPerson = deliveryPersonOptional
                                        .orElseThrow(
                                            () -> new EntityNotFoundException("Entregador não encontrado")
                                        );

        deliveryPerson.setLatitude(coordinatesRequest.latitude());
        deliveryPerson.setLongitude(coordinatesRequest.longitute());

        deliveryPersonRepository.save(deliveryPerson);
    }

    public void updateOrderStatus(Long orderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        OrderDeliveryStatus orderDeliveryStatus = customerOrder.nextDeliveryStatus();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder, orderDeliveryStatus);
    }

    public void previousOrderStatus(Long orderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        OrderDeliveryStatus orderDeliveryStatus = customerOrder.previousStatusDelivery();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder, orderDeliveryStatus);
    }

    public void refuseOrder(Long customerOrderId, String justification, Principal principal) {
            DeliveryPerson deliveryPerson = deliveryPersonRepository
                                            .findByEmail(principal.getName())
                                            .orElseThrow(DeliveryPersontNotFoundException::new);

            CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow();

            var refuseCustomerOrder = new RefuseCustomerOrder(
                customerOrderId, deliveryPerson.getId(), justification
            );

        refuseCustomerOrderRepository.save(refuseCustomerOrder);

        this.choiceDeliveryPersonWhenReady(customerOrder);
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

        this.choiceDeliveryPersonWhenReady(customerOrder);
    }
}
