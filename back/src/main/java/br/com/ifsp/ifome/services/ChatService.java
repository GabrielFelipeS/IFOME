package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.exceptions.global.CannotAccessTheChatException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChatService {

    private final CustomerOrderService customerOrderService;

    public ChatService(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    public void getChatClientDelivery(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatClientDeliveryOrElseThrow(customerOrder, loggedEmail, authorities);
    }

    public void getChatClientRestaurant(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatClientRestaurantOrElseThrow(customerOrder, loggedEmail, authorities);
    }

    public void getChatRestaurantDelivery(Long customerOrderId, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        CustomerOrder customerOrder = customerOrderService.findById(customerOrderId);
        this.canAccessTheChatRestaurantDeliveryOrElseThrow(customerOrder, loggedEmail, authorities);
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

    private boolean cannnotAccessTheChat(String userEmail, String loggedEmail,
                                         Collection<? extends GrantedAuthority> authorities, String role) {

        return !userEmail.equals(loggedEmail) || authorities.stream().noneMatch(auth -> auth.getAuthority().equals(role));
    }
}
