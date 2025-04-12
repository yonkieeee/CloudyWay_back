package org.example.hereapiservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.hereapiservice.schemas.DiscoverPlaceResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class HereApiService {

    private final RestTemplate restTemplate;
    private final String HERE_API_KEY;
    private final RabbitTemplate rabbitTemplate;
    private final String discoverBaseUrl;
    private final String geocodeBaseUrl;
    private final String bbox;
    private final ObjectMapper objectMapper;

    @Autowired
    public HereApiService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate, @Value("${here_api_key}") String HERE_API_KEY, @Value("${discover_base_url}") String discoverBaseUrl, @Value("${geocode_base_url}") String geocodeBaseUrl, @Value("${bbox}") String bbox) {
        this.restTemplate = restTemplate;
        this.HERE_API_KEY = HERE_API_KEY;
        this.rabbitTemplate = rabbitTemplate;
        this.discoverBaseUrl = discoverBaseUrl;
        this.geocodeBaseUrl = geocodeBaseUrl;
        this.bbox = bbox;
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<String> getLocationData(Double latitude, Double longitude, String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String formattedLatLon = String.format(Locale.ENGLISH, "%.6f,%.6f", latitude, longitude);

            String url = String.format("%s?at=%s&q=%s&apiKey=%s",
                    discoverBaseUrl, formattedLatLon, encodedQuery, HERE_API_KEY);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error getting location data");
        }
    }

    public List<DiscoverPlaceResponseDTO> discoverPlace(String placeName) {
        try {
            String url = String.format("%s?q=%s&in=bbox:%s&apiKey=%s",
                    discoverBaseUrl, placeName, bbox, HERE_API_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            List<DiscoverPlaceResponseDTO> results = new ArrayList<>();

            for (JsonNode item : items) {
                String title = item.path("title").asText();
                String id = item.path("id").asText();

                JsonNode address = item.path("address");
                String city = address.path("city").asText();
                String county = address.path("county").asText();
                String street = address.path("street").asText(null);
                String houseNumber = address.path("houseNumber").asText(null);

                JsonNode position = item.path("position");
                Map<String, String> coordinates = Map.of(
                        "lat", position.path("lat").asText(),
                        "lng", position.path("lng").asText()
                );
                DiscoverPlaceResponseDTO place = new DiscoverPlaceResponseDTO(
                        title, id, city, county, street, houseNumber, coordinates
                );
                results.add(place);
            }
            return results;
        } catch (JsonProcessingException e) {
            System.err.println("Json error");
            return Collections.emptyList();
        }
    }

    public ResponseEntity<String> geocodeLocationData(String houseNumber, String street, String city, String state, String postalCode, String country) {
        try {
            String url = String.format("%s?qq=houseNumber=%s;street=%s;city=%s;state=%s;postalCode=%s;country=%s&apiKey=%s",
                    geocodeBaseUrl, houseNumber, street, city, state, postalCode, country, HERE_API_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            rabbitTemplate.convertAndSend("Geocode", "", response.getBody());
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error getting location data");
        }
    }

    public ResponseEntity<String> geocodePlace(String placeName, String houseNumber, String street, String city) {
        try {
            String query = String.format("%s, %s %s, %s", placeName, houseNumber, street, city);

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = String.format("%s?q=%s&apiKey=%s",
                    geocodeBaseUrl, encodedQuery, HERE_API_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error getting location data");
        }
    }
}

