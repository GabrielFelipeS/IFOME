package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private static final double RAIO_DA_TERRA_KM = 6371.01; // Raio da Terra em quilômetros
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;

    public DeliveryService(DeliveryPersonRepository deliveryPersonRepository, CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
    }

    public List<DeliveryOrderResponse> getOrders(Principal principal) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllByDeliveryPerson(principal.getName());
        customerOrders.forEach(System.err::println);

        return customerOrders.stream().map(DeliveryOrderResponse::new).toList();
    }

    @Async  // TODO melhorar forma de busca
    public void choiceRestaurantWhenReady(CustomerOrder customerOrder) {
        boolean isNotReady = !customerOrder.getCurrentOrderStatus().equals(OrderStatus.PRONTO_PARA_ENTREGA);
        if(isNotReady) return;

        System.err.println(customerOrder.getRestaurantAddress());

        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findDeliveryPersonAvailable();

        deliveryPersons.stream().forEach(System.err::println);
        Address addressRestaurant = customerOrder.getRestaurantAddress();

        double minDistance = Double.MAX_VALUE;

        DeliveryPerson deliveryPersonChoice = deliveryPersons.get(0);

        for(DeliveryPerson deliveryPerson : deliveryPersons) {
            double distance = calculateDistance(addressRestaurant, deliveryPerson.getLatitude(), deliveryPerson.getLongitude());
            System.err.println(distance);
            if(distance < minDistance) {
                minDistance = distance;
                deliveryPersonChoice = deliveryPerson;
            }
        }

        customerOrder.setDeliveryPerson(deliveryPersonChoice);

        System.err.println("AQUI 1");
        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder, OrderStatus.NOVO);
    }

    private double calculateDistance(Address restaurantAddress, String latitudeDeliveryPerson, String longitudeDeliveryPerson) {
        double latRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLatitude()));
        double lonRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLongitude()));
        double latDeliveryPerson = Math.toRadians(Double.parseDouble(latitudeDeliveryPerson));
        double lonDeliveryPerson = Math.toRadians(Double.parseDouble(longitudeDeliveryPerson));

        double diffLatitude = latDeliveryPerson - latRestaurant;
        double diffLongitude = lonDeliveryPerson - lonRestaurant;

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

    public void updateOrderStatus(Long customerOrderId) {
        
    }

    public void previousOrderStatus(Long customerOrderId) {
    }

    public DeliveryPersonResponse getByEmail(String email) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByEmail(email).orElseThrow();
        return new DeliveryPersonResponse(deliveryPerson);
    }
}
