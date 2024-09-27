package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.services.DishService;
import br.com.ifsp.ifome.services.FileStorageService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@MultipartConfig
@RestController
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;
    private final FileStorageService fileStorageService;

    public DishController(DishService dishService, FileStorageService fileStorageService){
        this.dishService = dishService;
        this.fileStorageService = fileStorageService;
    }


    /*
    RequestPart só é usado quando o controller é anotado com multipart e o tipo do mapping é colocado como multi part
    como vai esta a seguir
     */
    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DishResponse> create(
            @Valid @RequestPart("dish")DishRequest dishRequest, UriComponentsBuilder ucb)
        throws IOException, MethodArgumentNotValidException{
        DishResponse dishResponse = dishService.create(dishRequest);

        URI locationOfNewDish = ucb
                .path("dish/{id}")
                .buildAndExpand(dishResponse.id())
                .toUri();
        return ResponseEntity.created(locationOfNewDish).body(dishResponse);
    }
}
