package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsGetChat;
import br.com.ifsp.ifome.docs.DocsPostChat;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.MessageRequest;
import br.com.ifsp.ifome.dto.response.ChatResponse;
import br.com.ifsp.ifome.dto.response.MessageResponse;
import br.com.ifsp.ifome.services.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @DocsGetChat
    @GetMapping("/client/delivery/{customerOrderId}")
    public ResponseEntity<ApiResponse> getChatClientDelivery(@PathVariable Long customerOrderId, Authentication authentication) {
        ChatResponse chatResponse = chatService.getChatClientDeliveryResponse(customerOrderId, authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", chatResponse, "Chat encontrado com sucesso!");
        return ResponseEntity.ok(apiResponse);
    }

    @DocsPostChat
    @PostMapping("/client/delivery/{customerOrderId}")
    public ResponseEntity<ApiResponse> addMesssageInChatClientDelivery(@PathVariable Long customerOrderId, @Valid @RequestBody MessageRequest messageRequest, Authentication authentication) {
        MessageResponse message = chatService.addMesssageInChatClientDelivery(customerOrderId, messageRequest.content(), authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", message, "Mensagem inserida com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DocsGetChat
    @GetMapping("/client/restaurant/{customerOrderId}")
    public ResponseEntity<ApiResponse> getChatClientRestaurant(@PathVariable Long customerOrderId,  Authentication authentication) {
        ChatResponse chatResponse = chatService.getChatClientRestaurantResponse(customerOrderId, authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", chatResponse, "Chat encontrado com sucesso!");
        return ResponseEntity.ok(apiResponse);
    }

    @DocsPostChat
    @PostMapping("/client/restaurant/{customerOrderId}")
    public ResponseEntity<ApiResponse> addMesssageInChatClientRestaurant(@PathVariable Long customerOrderId, @Valid @RequestBody MessageRequest messageRequest,Authentication authentication) {
        MessageResponse message  = chatService.addMesssageInChatClientRestaurant(customerOrderId, messageRequest.content(), authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", message, "Mensagem inserida com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DocsGetChat
    @GetMapping("/restaurant/delivery/{customerOrderId}")
    public ResponseEntity<ApiResponse> getChatRestaurantDelivery(@PathVariable Long customerOrderId, Authentication authentication) {
        ChatResponse chatResponse = chatService.getChatRestaurantDeliveryResponse(customerOrderId, authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", chatResponse, "Chat encontrado com sucesso!");
        return ResponseEntity.ok(apiResponse);
    }

    @DocsPostChat
    @PostMapping("/restaurant/delivery/{customerOrderId}")
    public ResponseEntity<ApiResponse> addMesssageInChatRestaurantDelivery(@PathVariable Long customerOrderId, @Valid @RequestBody MessageRequest messageRequest, Authentication authentication) {
        MessageResponse message  = chatService.addMesssageInChatRestaurantDelivery(customerOrderId, messageRequest.content(), authentication.getName(), authentication.getAuthorities());

        ApiResponse apiResponse = new ApiResponse("success", message, "Mensagem inserida com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
