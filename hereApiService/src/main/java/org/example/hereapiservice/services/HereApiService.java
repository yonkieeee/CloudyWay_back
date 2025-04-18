package org.example.hereapiservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.example.hereapiservice.dto.DiscoverPlaceResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hereapiservice.dto.DiscoverRequestDTO;
import org.example.hereapiservice.dto.PlaceResponseDTO;
import org.example.hereapiservice.mappers.DiscoverMapper;
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
    private final DiscoverMapper discoverMapper;
    private final TranslateService translateService;

    @Autowired
    public HereApiService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate,
                          @Value("${here_api_key}") String HERE_API_KEY,
                          @Value("${discover_base_url}") String discoverBaseUrl,
                          @Value("${geocode_base_url}") String geocodeBaseUrl,
                          @Value("${bbox}") String bbox,
                          DiscoverMapper discoverMapper,
                          TranslateService translateService) {
        this.restTemplate = restTemplate;
        this.HERE_API_KEY = HERE_API_KEY;
        this.rabbitTemplate = rabbitTemplate;
        this.discoverBaseUrl = discoverBaseUrl;
        this.geocodeBaseUrl = geocodeBaseUrl;
        this.bbox = bbox;
        this.objectMapper = new ObjectMapper();
        this.discoverMapper = discoverMapper;
        this.translateService = translateService;
    }

    public List<DiscoverPlaceResponseDTO> getLocationData(String json) {
        try {
            DiscoverRequestDTO request = objectMapper.readValue(json, DiscoverRequestDTO.class);
            String formattedLatLon = String.format(Locale.ENGLISH, "%.6f,%.6f", request.getLatitude(), request.getLongitude());
            String url = String.format("%s?at=%s&q=%s&apiKey=%s",
                    discoverBaseUrl, formattedLatLon, request.getQuery(), HERE_API_KEY);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceResponseDTO.class);
            List<PlaceResponseDTO> places = objectMapper.readValue(items.toString(), listType);
            return discoverMapper.toListDiscoverPlaceResponseDTO(places);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<DiscoverPlaceResponseDTO> discoverPlace(String placeName) {
        try {
            String url = String.format("%s?q=%s&in=bbox:%s&apiKey=%s",
                    discoverBaseUrl, translateService.translate(placeName), bbox, HERE_API_KEY);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceResponseDTO.class);
            List<PlaceResponseDTO> places = objectMapper.readValue(items.toString(), listType);

            return discoverMapper.toListDiscoverPlaceResponseDTO(places);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
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

