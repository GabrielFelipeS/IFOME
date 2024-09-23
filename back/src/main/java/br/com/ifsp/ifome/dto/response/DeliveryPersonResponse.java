package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.BankAccount;
import br.com.ifsp.ifome.entities.DeliveryPerson;

import java.time.LocalDate;
import java.util.List;

public record DeliveryPersonResponse(
        Long id,
        String name,
        String cpf,
        String email,
        LocalDate dateOfBirth,
        String typeOfVehicle,
        String plate,
        String telephone,
        String cnhNumber,
        LocalDate cnhValidity,
        String vehicleDocument,
        List<Address> address,
        BankAccount bankAccount

) {
    public DeliveryPersonResponse(DeliveryPerson deliveryPerson){
        this(deliveryPerson.getId(),
        deliveryPerson.getName(),
        deliveryPerson.getCpf(),
        deliveryPerson.getEmail(),
        deliveryPerson.getDateOfBirth(),
        deliveryPerson.getTypeOfVehicle(),
        deliveryPerson.getPlate(),
        deliveryPerson.getTelephone(),
        deliveryPerson.getCnhNumber(),
        deliveryPerson.getCnhValidity(),
        deliveryPerson.getVehicleDocument(),
        deliveryPerson.getAddress(),
        deliveryPerson.getBankAccount());
    }
}
