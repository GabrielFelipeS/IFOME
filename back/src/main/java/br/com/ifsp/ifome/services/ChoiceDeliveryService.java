package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ChoiceDeliveryService {
    private static final double RAIO_DA_TERRA_KM = 6371.01;
    private static final int ONE_MINUTE_AND_FIVE_SECONDS_IN_SECONDS = 65;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final DeliveryPersonRepository deliveryPersonRepository;

    public ChoiceDeliveryService(CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService, DeliveryPersonRepository deliveryPersonRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.deliveryPersonRepository = deliveryPersonRepository;
    }


    private void scheduleChooseAnotherDeliveryPerson(CustomerOrder customerOrder) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            System.err.println("Tarefa executada no futuro por " + Thread.currentThread().getName());
            choiceDeliveryPersonWhenReady(customerOrder);
        }, ONE_MINUTE_AND_FIVE_SECONDS_IN_SECONDS, TimeUnit.SECONDS);

        scheduler.shutdown();
    }

    // TODO melhorar forma de busca
    // TODO refatorar essa metodo
    @Async
    public void choiceDeliveryPersonWhenReady(CustomerOrder customerOrder) {
        boolean isNotReady = !customerOrder.getCurrentOrderClientStatus().equals(OrderClientStatus.PRONTO_PARA_ENTREGA);
        System.err.println( customerOrder.getCurrentOrderClientStatus()+ " " + isNotReady);
        if(isNotReady) return;

        System.err.println("AQUI NÂO");
        System.err.println(customerOrder.getRestaurantAddress());
        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findDeliveryPersonAvailable(customerOrder.getId());
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

        System.err.println("AQUI 3");

        if(deliveryPersonChoice == null) {
            System.err.println("ENTROU NO IF");
            this.scheduleChooseAnotherDeliveryPerson(customerOrder);
            System.err.println("Executou o choose");
            return;
        }

        System.err.println("AQUI 4");
        customerOrder.setDeliveryPerson(deliveryPersonChoice);
        System.err.println("AQUI 5");
        System.err.println("Min distance: " + minDistance);
        System.err.println("Entregador escolhido: " + customerOrder.getDeliveryPerson().getEmail());

        double preciseDelivery = minDistance * 1;
        customerOrder.setDeliveryCost(preciseDelivery);
        customerOrder.nextDeliveryStatus();

        customerOrder.getOrderInfo().forEach(System.out::println);
        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder);
    }

    private double calculateDistance(Address restaurantAddress, String latitudeDeliveryPerson, String longitudeDeliveryPerson) {
        System.err.println("AQUI 5");
        System.err.println("latRestaurant " + restaurantAddress.getLatitude());
        System.err.println("lonRestaurant " + restaurantAddress.getLongitude());
        System.err.println("latDeliveryPerson " + latitudeDeliveryPerson);
        System.err.println("lonDeliveryPerson " + longitudeDeliveryPerson);

        double latRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLatitude()));
        double lonRestaurant = Math.toRadians(Double.parseDouble(restaurantAddress.getLongitude()));
        double latDeliveryPerson = Math.toRadians(Double.parseDouble(latitudeDeliveryPerson));
        double lonDeliveryPerson = Math.toRadians(Double.parseDouble(longitudeDeliveryPerson));

        System.err.println("Passou das conversões");

        double diffLatitude = latDeliveryPerson - latRestaurant;
        double diffLongitude = lonDeliveryPerson - lonRestaurant;

        System.err.println("Passou das diferenças");

//        System.err.println("Latitude");
//        System.err.println(latRestaurant);
//        System.err.println(latDeliveryPerson);
//
//        System.err.println("Longitude");
//        System.err.println(lonRestaurant);
//        System.err.println(lonDeliveryPerson);
//
//        System.err.println("Diferença");
//        System.err.println(diffLatitude);
//        System.err.println(diffLongitude);

        double diffAngular = Math.pow(Math.sin(diffLatitude / 2), 2)
            + Math.cos(latRestaurant) * Math.cos(latDeliveryPerson) * Math.pow(Math.sin(diffLongitude / 2), 2);

        System.err.println("Passou da diff angular " + diffAngular);

        double anguloCentral = 2 * Math.atan2(Math.sqrt(diffAngular), Math.sqrt(1 - diffAngular));

        System.err.println("Passou da angulo central " + anguloCentral);

        return RAIO_DA_TERRA_KM * anguloCentral;
    }
}
