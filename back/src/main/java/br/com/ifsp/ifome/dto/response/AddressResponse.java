package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;

public record AddressResponse(
    Integer id,
    String nameAddress,
    String cep,
    String neighborhood,
    String city,
    String state,
    String address,
    String number,
    String complement,
    String details,
    String typeResidence,
    String latitude,
    String longitude
) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getNameAddress(),
            address.getCep(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getAddress(),
            address.getNumber(),
            address.getComplement(),
            address.getDetails(),
            address.getTypeResidence(),
            address.getLatitude(),
            address.getLongitude()
        );
    }
}
