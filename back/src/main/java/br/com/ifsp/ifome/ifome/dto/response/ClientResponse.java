package br.com.ifsp.ifome.ifome.dto.response;

import br.com.ifsp.ifome.ifome.entities.Client;

import java.time.LocalDate;

public record ClientResponse(
    Long id,
    String email,
    LocalDate dateOfBirth,
    String cpf,
    String typeResidence,
    String cep,
    String address,
    String paymentMethods
) {
    public ClientResponse(Client client) {
        this(client.getId(), client.getEmail(), client.getDateOfBirth(),
            client.getCpf(), client.getTypeResidence(), client.getCep(),
            client.getAddress(), client.getPaymentMethods());
    }


}
