package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.BankAccount;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

public record DeliveryPersonResponse(
        Long id,
        String name,
        String cpf,
        String email,
        LocalDate dateOfBirth,
        String typeOfVehicle,
        String telephone,
        String cnh,
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
        deliveryPerson.getTelephone(),
        deliveryPerson.getCnh(),
        deliveryPerson.getVehicleDocument(),
        deliveryPerson.getAddress(),
        deliveryPerson.getBankAccount());
    }
}
