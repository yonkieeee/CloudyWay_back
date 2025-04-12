package org.example.hereapiservice.controllers;

import org.example.hereapiservice.schemas.*;
import org.example.hereapiservice.services.HereApiService;
import org.example.hereapiservice.services.TranslateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "*")
public class HereApiController {

    private final HereApiService service;
    private final ObjectMapper objectMapper;
    private final TranslateService translateService;

    public HereApiController(HereApiService service, TranslateService translateService) {
        this.service = service;
        this.objectMapper = new ObjectMapper();
        this.translateService = translateService;
    }

    @GetMapping
    public ResponseEntity<String> getLocation(@RequestParam String json) {
        try {
            DiscoverRequestDTO request = objectMapper.readValue(json, DiscoverRequestDTO.class);
            return service.getLocationData(request.getLatitude(), request.getLongitude(), request.getQuery());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Invalid JSON format");
        }
    }

    @GetMapping("/discover")
    public ResponseEntity<List<DiscoverPlaceResponseDTO>> discoverPlace(@RequestBody DiscoverPlaceRequestDTO json) {
        try {
            String placeName = json.getPlaceName();
            String languageCode = json.getLanguageCode();
            String text = translateService.translate(placeName, languageCode);
            return ResponseEntity.ok(service.discoverPlace(text));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/geocode")
    public ResponseEntity<String> geocodeLocation(@RequestParam String json) {
        try {
            GeocodeRequestDTO request = objectMapper.readValue(json, GeocodeRequestDTO.class);
            return service.geocodeLocationData(request.getHouseNumber(), request.getStreet(), request.getCity(), request.getState(), request.getPostalCode(), request.getCountry());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Invalid JSON format");
        }
    }
    @GetMapping("/geocode/places")
    public ResponseEntity<String> geocodePlace(@RequestParam String json) {
        try {
            GeocodePlaceRequestDTO request = objectMapper.readValue(json, GeocodePlaceRequestDTO.class);
            return service.geocodePlace(request.getPlaceName(), request.getHouseNumber(), request.getStreet(), request.getCity());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Invalid JSON format");
        }
    }
}
