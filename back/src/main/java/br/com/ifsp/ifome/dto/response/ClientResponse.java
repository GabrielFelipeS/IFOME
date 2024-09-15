package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.Client;

import java.time.LocalDate;
import java.util.List;

public record ClientResponse(
    Long id,
    String email,
    LocalDate dateOfBirth,
    String cpf,
    List<Address> address
) {
    public ClientResponse(Client client) {
        this(client.getId(), client.getEmail(), client.getDateOfBirth(),
            client.getCpf(), client.getAddress());
    }
}
