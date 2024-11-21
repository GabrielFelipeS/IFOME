package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.services.ChatService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/chat/")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/client/delivery/{customerOrderId}")
    public void getChatClientDelivery(@PathVariable Long customerOrderId, Authentication authentication) {
        chatService.getChatClientDelivery(customerOrderId, authentication.getName(), authentication.getAuthorities());
    }

    @GetMapping("/client/restaurant/{customerOrderId}")
    public void getChatClientRestaurant(@PathVariable Long customerOrderId, Principal principal, Authentication authentication) {
        chatService.getChatClientRestaurant(customerOrderId, principal.getName(), authentication.getAuthorities());
    }

    @GetMapping("/restaurant/delivery/{customerOrderId}")
    public void getChatDelivery(@PathVariable Long customerOrderId, Principal principal, Authentication authentication) {
        chatService.getChatRestaurantDelivery(customerOrderId, principal.getName(), authentication.getAuthorities());
    }

}
