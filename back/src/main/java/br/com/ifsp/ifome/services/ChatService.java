package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.ChatResponse;
import br.com.ifsp.ifome.dto.response.MessageResponse;
import br.com.ifsp.ifome.entities.*;
import br.com.ifsp.ifome.exceptions.global.CannotAccessTheChatException;
import br.com.ifsp.ifome.repositories.ClientDeliveryChatRepository;
import br.com.ifsp.ifome.repositories.ClientRestaurantChatRepository;
import br.com.ifsp.ifome.repositories.RestaurantDeliveryChatRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChatService {
    private final CustomerOrderService customerOrderService;
    private final ClientRestaurantChatRepository clientRestaurantChatRepository;
    private final ClientDeliveryChatRepository clientDeliveryChatRepository;
    private final RestaurantDeliveryChatRepository restaurantDeliveryChatRepository;
    private final PusherService pusherService;

    public ChatService(CustomerOrderService customerOrderService, ClientRestaurantChatRepository clientRestaurantChatRepository, ClientDeliveryChatRepository clientDeliveryChatRepository, RestaurantDeliveryChatRepository restaurantDeliveryChatRepository, PusherService pusherService) {
        this.customerOrderService = customerOrderService;
        this.clientRestaurantChatRepository = clientRestaurantChatRepository;
        this.clientDeliveryChatRepository = clientDeliveryChatRepository;
        this.restaurantDeliveryChatRepository = restaurantDeliveryChatRepository;
        this.pusherService = pusherService;
    }

    public ClientDeliveryChat getChatClientDelivery(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatClientDeliveryOrElseThrow(customerOrder, loggedEmail, authorities);

        return clientDeliveryChatRepository.findByCustomerOrderId(customerOrderId).orElse(
            this.generatedClientDeliveryChat(customerOrder)
        );
    }

    public ChatResponse getChatClientDeliveryResponse(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        return ChatResponse.from(this.getChatClientDelivery(customerOrderId, loggedEmail,authorities));
    }

    public MessageResponse addMesssageInChatClientDelivery(Long customerOrderId, String content, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        ClientDeliveryChat chat = this.getChatClientDelivery(customerOrderId, loggedEmail, authorities);
        Message message = chat.addMessage(content, loggedEmail, authorities);

        clientDeliveryChatRepository.save(chat);

        pusherService.addMessageInChat(chat, message);

        return MessageResponse.from(message);
    }

    public ClientRestaurantChat getChatClientRestaurant(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatClientRestaurantOrElseThrow(customerOrder, loggedEmail, authorities);

        return clientRestaurantChatRepository.findByCustomerOrderId(customerOrderId).orElse(
            this.generatedClientRestaurantChat(customerOrder)

        );
    }

    public ChatResponse getChatClientRestaurantResponse(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        return ChatResponse.from(this.getChatClientRestaurant(customerOrderId, loggedEmail,authorities));
    }

    public MessageResponse addMesssageInChatClientRestaurant(Long customerOrderId, String content, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        ClientRestaurantChat chat = this.getChatClientRestaurant(customerOrderId, loggedEmail, authorities);
        Message message = chat.addMessage(content, loggedEmail, authorities);

        clientRestaurantChatRepository.save(chat);

        pusherService.addMessageInChat(chat, message);

        return MessageResponse.from(message);
    }

    public RestaurantDeliveryChat getChatRestaurantDelivery(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatRestaurantDeliveryOrElseThrow(customerOrder, loggedEmail, authorities);

        return restaurantDeliveryChatRepository.findByCustomerOrderId(customerOrderId).orElse(
            this.generatedRestaurantDeliveryChat(customerOrder)
        );
    }


    public ChatResponse getChatRestaurantDeliveryResponse(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        return ChatResponse.from(this.getChatRestaurantDelivery(customerOrderId, loggedEmail, authorities));
    }

    public MessageResponse addMesssageInChatRestaurantDelivery(Long customerOrderId, String content, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        RestaurantDeliveryChat chat = this.getChatRestaurantDelivery(customerOrderId, loggedEmail, authorities);
        Message message = chat.addMessage(content, loggedEmail, authorities);

        restaurantDeliveryChatRepository.save(chat);

        pusherService.addMessageInChat(chat, message);

        return MessageResponse.from(message);
    }

    private void canAccessTheChatClientDeliveryOrElseThrow(CustomerOrder customerOrder, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        if(
            this.cannnotAccessTheChat(customerOrder.getClientEmail(), loggedEmail, authorities, "ROLE_CLIENT") &&
            this.cannnotAccessTheChat(customerOrder.getDeliveryEmail(), loggedEmail, authorities, "ROLE_DELIVERY")
        ) {
            throw new CannotAccessTheChatException("Você não tem permissão para acessar o chat!");
        }
    }

    private void canAccessTheChatClientRestaurantOrElseThrow(CustomerOrder customerOrder, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        if(
            this.cannnotAccessTheChat(customerOrder.getClientEmail(), loggedEmail, authorities, "ROLE_CLIENT") &&
            this.cannnotAccessTheChat(customerOrder.getRestaurantEmail(), loggedEmail, authorities, "ROLE_RESTAURANT")
        ) {
            throw new CannotAccessTheChatException("Você não tem permissão para acessar o chat!");
        }
    }

    private void canAccessTheChatRestaurantDeliveryOrElseThrow(CustomerOrder customerOrder, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        if(
            this.cannnotAccessTheChat(customerOrder.getDeliveryEmail(), loggedEmail, authorities, "ROLE_DELIVERY") &&
            this.cannnotAccessTheChat(customerOrder.getRestaurantEmail(), loggedEmail, authorities, "ROLE_RESTAURANT")
        ) {
            throw new CannotAccessTheChatException("Você não tem permissão para acessar o chat!");
        }
    }

    private boolean cannnotAccessTheChat(String associatedEmailWithOrder, String userEmail,
                                         Collection<? extends GrantedAuthority> authorities, String expectedRole) {

        return !associatedEmailWithOrder.equals(userEmail) || authorities.stream().noneMatch(auth -> auth.getAuthority().equals(expectedRole));
    }

    public void generatedChats(CustomerOrder customerOrder){
        this.generatedClientDeliveryChat(customerOrder);
        this.generatedClientRestaurantChat(customerOrder);
        this.generatedRestaurantDeliveryChat(customerOrder);
    }

    private ClientDeliveryChat generatedClientDeliveryChat(CustomerOrder customerOrder) {
        return clientDeliveryChatRepository.save(new ClientDeliveryChat(customerOrder));
    }

    private ClientRestaurantChat generatedClientRestaurantChat(CustomerOrder customerOrder) {
        return clientRestaurantChatRepository.save(new ClientRestaurantChat(customerOrder));
    }

    private RestaurantDeliveryChat generatedRestaurantDeliveryChat(CustomerOrder customerOrder) {
        return restaurantDeliveryChatRepository.save(new RestaurantDeliveryChat(customerOrder));
    }
}
