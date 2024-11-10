package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.dto.response.PusherDeliveryOrderResponse;
import br.com.ifsp.ifome.entities.*;
import br.com.ifsp.ifome.exceptions.delivery.DeclineNotAvailableException;
import br.com.ifsp.ifome.exceptions.delivery.DeliveryPersontNotFoundException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.repositories.OrderInfoDeliveryRepository;
import br.com.ifsp.ifome.repositories.RefuseCustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento das entregas de pedidos.
 * Inclui operações para selecionar entregadores, calcular distâncias,
 * atualizar o status do pedido e gerenciar recusas de entregas.
 */

@Service
public class DeliveryService {
    private static final double RAIO_DA_TERRA_KM = 6371.01; // Raio da Terra em quilômetros
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final RefuseCustomerOrderRepository refuseCustomerOrderRepository;
    private final OrderInfoDeliveryRepository orderInfoDeliveryRepository;

    public DeliveryService(DeliveryPersonRepository deliveryPersonRepository, CustomerOrderRepository customerOrderRepository, OrderStatusUpdateService orderStatusUpdateService, RefuseCustomerOrderRepository refuseCustomerOrderRepository, OrderInfoDeliveryRepository orderInfoDeliveryRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.refuseCustomerOrderRepository = refuseCustomerOrderRepository;
        this.orderInfoDeliveryRepository = orderInfoDeliveryRepository;
    }

    public List<DeliveryOrderResponse> getOrders(Principal principal) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllByDeliveryPerson(principal.getName());
        customerOrders.forEach(System.err::println);

        return customerOrders.stream().map(DeliveryOrderResponse::new).toList();
    }

    // TODO melhorar forma de busca
    // TODO refatorar essa metodo

    /**
     * Escolhe um entregador para o pedido quando o pedido está como o status pronto para entrega.
     *
     * @param customerOrder Pedido do cliente que está pronto para entrega
     */
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
            System.err.println("ENTROU NO IF");
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

    /**
     * Calcula a distância entre o restaurante e o entregador por meio de um cálculo utilizando a latitude geográfica de ambos
     *
     * @param restaurantAddress Endereço do restaurante para obter a informação de latitude e longitude do restaurante
     * @param latitudeDeliveryPerson Para obter a informação de latitude do entregador
     * @param longitudeDeliveryPerson Para obter a informação de longitude do entregador
     * @return A distância cálculada em km
     */
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


    /**
     * Atualiza as informações de coordenadas do entregador logado
     *
     * @param coordinatesRequest Novas informações de coordenadas do entregador
     * @param principal Identificação do usuário logado
     */
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


    /**
     * Atualiza o status do pedido para o próximo status definido na sequência
     *
     * @param orderId Id do pedido que deve ser atualizado
     */
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

    /**
     *Reverte o stautus do pedido para o status anterior
     *
     * @param orderId Id do pedido que deve ser atualizado
     */
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


    /**
     * Registra a recusa de entrega de um pedido pelo entregador
     *
     * @param customerOrderId Identificador do pedido que foi recusado
     * @param justification Justifica da recusa da entrega
     * @param principal Identificação do usuário entregador logado
     */
    public void refuseOrder(Long customerOrderId, String justification, Principal principal) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository
                                        .findByEmail(principal.getName())
                                        .orElseThrow(DeliveryPersontNotFoundException::new);

        CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId).orElseThrow();

        var currentOrderDelivery = customerOrder.getOrderInfoDelivery();

        if(currentOrderDelivery.size() >= 6) {
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

        this.choiceDeliveryPersonWhenReady(customerOrder);
    }

    /***
     * Retorna as informações do entregador pelo o e-mail
     *
     * @param email Email do entregador
     * @return Informações do entregador encontrado com o e-mail
     */
    public DeliveryPersonResponse getByEmail(String email) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByEmail(email).orElseThrow();
        return new DeliveryPersonResponse(deliveryPerson);
    }

    /**
     *Simula a escolha de um entregador de um pedido
     *
     * @param customerOrderId Identificador do pedido
     */
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


    /**
     * Obtem o pedido associado ao entregador logado, caso exista
     *
     * @param principal Informações do usuário logado
     * @return Retorna o pedido ativo que está associado ao entregador
     */
    public Optional<PusherDeliveryOrderResponse> getCustomerOrderId(Principal principal) {
        var customerOrdes = customerOrderRepository.findActiveOrderDeliveryPerson(principal.getName());

        if(customerOrdes.isEmpty()) {
            return Optional.empty();
        }

        CustomerOrder customerOrder = customerOrdes.get(0);

        DeliveryOrderResponse deliveryOrderResponse = new DeliveryOrderResponse(customerOrder);

        return Optional.of(PusherDeliveryOrderResponse.from(customerOrder, deliveryOrderResponse));
    }

    /**
     *Determina o status de disponibilidade do entregador
     *
     * @param principal Informações do usuário logado
     * @return Mensagem que indica o status atual de disponibilidade do entregador logado
     */
    public String setAvailable(Principal principal) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByEmail(principal.getName()).get();

        String available = deliveryPerson.getAvailable();
        deliveryPerson.setAvailable(available.equals("Disponível")? "Indisponível" : "Disponível");
        deliveryPersonRepository.save(deliveryPerson);

        return String.format("Você está %s!", deliveryPerson.getAvailable());
    }
}
