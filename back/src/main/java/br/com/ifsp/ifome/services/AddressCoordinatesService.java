package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.exceptions.delivery.CoordinatesException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class AddressCoordinatesService {
    private static final Logger logger = LoggerFactory.getLogger(AddressCoordinatesService.class);

    /**
     *Cria um {@code Address} contendo latitude e longitude baseado nos valores de {@code AddressRequest}
     *
     * @param addressRequest Informações do endereço
     * @return Informações do endereço contendo latitude e longitude
     */
    public Address createAddressWithCoordinates(AddressRequest addressRequest) {
        String endereco = String.format("%s, %s, %s, %s, %s",
            addressRequest.address(), addressRequest.number(),
            addressRequest.city(), addressRequest.state(), "Brasil");

        try {
            HttpURLConnection connection = this.createConnection(endereco);

            String response = this.readTree(connection);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.isArray() && !jsonNode.isEmpty()) {
                JsonNode location = jsonNode.get(0);

                String latitude = location.get("lat").asText();
                String longitude = location.get("lon").asText();

                this.loggar(location, latitude, longitude);

                return new Address(addressRequest, latitude, longitude);
            }
        } catch (Exception e) {
            logger.error("Erro ao obter coordenadas: {}", e.getMessage());
        }

        throw new CoordinatesException("Localização não encontrada, por favor insira outro endereço");
    }

    /**
     *
     * @param endereco Endereço a ser buscado
     * @return Uma conexão com o site nominatim
     * @throws IOException Caso tenha problemas na codificação
     */
    // TODO isso poderia ser movido para uma classe a parte
    private HttpURLConnection createConnection(String endereco) throws IOException {
        String enderecoCodificado = URLEncoder.encode(endereco, "UTF-8");
        String urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + enderecoCodificado;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        return connection;
    }

    /**
     * Realiza a leitura do json devolvido pela conexão.
     *
     * @param connection Conexão com um site
     * @return Retorna o json no formato de uma string devolvido pela requisição da conexão
     * @throws IOException Caso tenha problemas na codificação
     */
    private String readTree(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void loggar(JsonNode location, String latitude, String longitude) {
        logger.info(location.toString());
        logger.info("Latitude: {}", latitude);
        logger.info("Longitude: {}", longitude);
    }

}
