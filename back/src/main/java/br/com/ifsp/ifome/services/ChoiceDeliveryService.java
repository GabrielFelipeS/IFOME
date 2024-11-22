package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.LoggingAspect;
import br.com.ifsp.ifome.entities.*;
import br.com.ifsp.ifome.exceptions.client.CustomerNotFoundInCartException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.repositories.OrderInfoDeliveryRepository;
import br.com.ifsp.ifome.repositories.RefuseCustomerOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ChoiceDeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static final double RAIO_DA_TERRA_KM = 6371.01;
    private static final int ONE_MINUTE_AND_FIVE_SECONDS_IN_SECONDS = 65;
    private static final int FIVE_MINUTES_AND_FIVE_SECONDS_IN_SECONDS = 305;

    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final RefuseCustomerOrderRepository refuseCustomerOrderRepository;
    private final EmailService emailService;
    private final OrderInfoDeliveryRepository orderInfoDeliveryRepository;

    public ChoiceDeliveryService(CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService, DeliveryPersonRepository deliveryPersonRepository, RefuseCustomerOrderRepository refuseCustomerOrderRepository, EmailService emailService, OrderInfoDeliveryRepository orderInfoDeliveryRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.refuseCustomerOrderRepository = refuseCustomerOrderRepository;
        this.emailService = emailService;
        this.orderInfoDeliveryRepository = orderInfoDeliveryRepository;
    }


    public void scheduleChooseAnotherDeliveryPerson(CustomerOrder customerOrder, int time) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        logger.warn("Procurando outro entregador em 1 minuto");
        scheduler.schedule(() -> {
            logger.warn("Tarefa executada no futuro por {}", Thread.currentThread().getName());
            this.choiceDeliveryPersonWhenReady(customerOrder);
        }, time, TimeUnit.SECONDS);

        scheduler.shutdown();
    }

    public void scheduleChooseAnotherDeliveryPerson(CustomerOrder customerOrder) {
      this.scheduleChooseAnotherDeliveryPerson(customerOrder, ONE_MINUTE_AND_FIVE_SECONDS_IN_SECONDS);
    }

    // TODO melhorar forma de busca
    // TODO refatorar essa metodo
    @Async
    public void choiceDeliveryPersonWhenReady(CustomerOrder customerOrder) {
        boolean isNotReady = !customerOrder.getCurrentOrderClientStatus().equals(OrderClientStatus.PRONTO_PARA_ENTREGA);
        if(isNotReady) return;

        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findDeliveryPersonAvailable(customerOrder.getId());

        deliveryPersons.forEach(delivery -> logger.info(delivery.toString()));

        Address addressRestaurant = customerOrder.getRestaurantAddress();

        double minDistance = Double.MAX_VALUE;

        DeliveryPerson deliveryPersonChoice = null;

        for(DeliveryPerson deliveryPerson : deliveryPersons) {
            double distance = calculateDistance(addressRestaurant, deliveryPerson.getLatitude(), deliveryPerson.getLongitude());
            if(distance < minDistance) {
                minDistance = distance;
                deliveryPersonChoice = deliveryPerson;
            }
        }

        if(deliveryPersonChoice == null) {
            logger.warn("Nenhum entregador encontrado");
            this.scheduleChooseAnotherDeliveryPerson(customerOrder);
            return;
        }

        customerOrder.setDeliveryPerson(deliveryPersonChoice);

        customerOrder.newDeliveryPersonInfo();

        logger.warn("Min distance: {}", minDistance);
        logger.warn("Entregador escolhido: {}", customerOrder.getDeliveryPerson().getEmail());

        double preciseDelivery = minDistance * 1;

        logger.warn("Setando delivery cost");
        customerOrder.setDeliveryCost(preciseDelivery);

        logger.warn("Para o próximo status");

        logger.warn("Salvando");
        customerOrderRepository.save(customerOrder);
//        customerOrderRepository.flush();

//        logger.warn("Proximo status: {}", customerOrder.getOrderInfoDelivery());
//        customerOrder.getOrderInfo().forEach(order -> logger.info(order.toString()));

        logger.warn("Salvando novo status");

        orderStatusUpdateService.updateStatusOrderToRestaurant(customerOrder);

        logger.warn("Iniciando scheduler para esperar");

        this.transferOfferToNextDeliverer(customerOrder.getId(), deliveryPersonChoice.getId());

        this.sendEmailWhenRequestedHasThreeRefused(customerOrder);

        logger.warn("Chegou no fim do método!");
    }

    public void transferOfferToNextDeliverer(Long customerOrderId, Long deliveryPersonChoiceId, int time) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        logger.warn("Transferindo para outro entregador, em caso de sem resposta em 5 minutos");

        scheduler.schedule(() -> {
            CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId)
                .orElseThrow(CustomerNotFoundInCartException::new);

            boolean isTheDriver = customerOrder.getDeliveryPersonId().equals(deliveryPersonChoiceId);
            boolean theOrderIsNew = customerOrder.getOrderInfoDelivery().size() <= 1;

            boolean isToTransfer = isTheDriver && theOrderIsNew;

            logger.warn("Para transferir: {}", isToTransfer);

            if(isToTransfer) {
                logger.warn("Iniciando transferencia");

                customerOrder.setDeliveryPerson(null);

                customerOrderRepository.save(customerOrder);

                customerOrderRepository.flush();

                var deliveryPersonInfo = customerOrder.resetDeliveryPersonInfo();

                orderInfoDeliveryRepository.deleteAll(deliveryPersonInfo);

                orderInfoDeliveryRepository.flush();

                logger.warn("Status limpo");

                var refuseCustomerOrder = new RefuseCustomerOrder(
                    customerOrderId, deliveryPersonChoiceId, "Tempo de espera excedido"
                );

                refuseCustomerOrderRepository.save(refuseCustomerOrder);

                refuseCustomerOrderRepository.flush();

                logger.warn("Id do pedido: {}",  customerOrderId);

                orderStatusUpdateService.updateStatusCanceledToDeliverer(customerOrderId);

                this.choiceDeliveryPersonWhenReady(customerOrder);
                logger.warn("scheduler finalizado");
            }
        }, time, TimeUnit.SECONDS);

//        scheduler.shutdown();
    }


     public void transferOfferToNextDeliverer(Long customerOrderId, Long deliveryPersonChoiceId) {
        this.transferOfferToNextDeliverer(customerOrderId, deliveryPersonChoiceId, FIVE_MINUTES_AND_FIVE_SECONDS_IN_SECONDS);
    }


    private void sendEmailWhenRequestedHasThreeRefused(CustomerOrder customerOrder) {
        var refusedCustomeOrders = refuseCustomerOrderRepository.findRefuseCustomerOrderByCustomerOrderId(customerOrder.getId());

        if(refusedCustomeOrders.size() < 3) return;

        emailService.sendEmailClientWhenRequestHasTrheeRefused(customerOrder);
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
