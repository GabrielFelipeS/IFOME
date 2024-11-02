package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.exceptions.CoordinatesException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddressCoordinatesService {

    public Address createAddressWithCoordinates(AddressRequest addressRequest) {
        Map<String, String> latitudeAndLongitude = new HashMap<>();
        String endereco = String.format("%s, %s, %s, %s, %s", addressRequest.address(), addressRequest.number(),addressRequest.city(), addressRequest.state(), "Brasil");
        try {
            // Codifica o endereço para ser utilizado na URL
            String enderecoCodificado = URLEncoder.encode(endereco, "UTF-8");
            String urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + enderecoCodificado;

            // Cria a conexão HTTP
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Lê a resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parseia o JSON usando Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());

            if (jsonNode.isArray() && !jsonNode.isEmpty()) {
                JsonNode location = jsonNode.get(0);
                System.out.println(location);
                String latitude = location.get("lat").asText();
                String longitude = location.get("lon").asText();

                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);

                return new Address(addressRequest, latitude, longitude);
            } else {
                System.out.println("Localização não encontrada.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao obter coordenadas: " + e.getMessage());
            throw new CoordinatesException();
        }

        return new Address(addressRequest, "latitude", "longitude");
    }

}
