package org.example.hereapiservice.controllers;

import org.example.hereapiservice.dto.*;
import org.example.hereapiservice.services.HereApiService;
import org.example.hereapiservice.services.TranslateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public HereApiController(HereApiService service) {
        this.service = service;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping
    public ResponseEntity<List<DiscoverPlaceResponseDTO>> getLocation(@RequestParam String json) {
        try {
            return ResponseEntity.ok(service.getLocationData(json));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/discover")
    public ResponseEntity<List<DiscoverPlaceResponseDTO>> discoverPlace(@RequestBody DiscoverPlaceRequestDTO json) {
        try {
            return ResponseEntity.ok(service.discoverPlace(json.getPlaceName()));
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
