package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPersonService {
    private final DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository){
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    public DeliveryPersonResponse create(DeliveryPersonRequest deliveryPersonRequest){
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequest);
        deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return new DeliveryPersonResponse(deliveryPerson);
    }
}
