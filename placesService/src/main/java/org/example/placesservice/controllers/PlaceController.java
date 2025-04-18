package org.example.placesservice.controllers;

import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.models.PlaceModel;
import org.example.placesservice.services.OpenSearchService;
import org.example.placesservice.services.impl.PlaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/places")
public class PlaceController {

    private final PlaceServiceImpl placeService;
    private final OpenSearchService openSearchService;

    @Autowired
    public PlaceController(PlaceServiceImpl placeService, OpenSearchService openSearchService) {
        this.placeService = placeService;
        this.openSearchService = openSearchService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlace(@RequestParam String placeName) {
        try {
            return ResponseEntity.ok(placeService.getPlaceByPlaceName(placeName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<PlaceDTO>> getCities(@PathVariable String city) {
        try {
            return ResponseEntity.ok(placeService.getAllPlacesByCity(city));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllPlaces")
    public ResponseEntity<List<PlaceDTO>> getAllCities() {
        try {
            return ResponseEntity.ok(placeService.getAllPlaces());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/addPlace")
    public ResponseEntity<String> createPlace(@RequestBody PlaceModel place) {
        try {
            placeService.save(place);
            openSearchService.addPlacesToIndex(place);
            return ResponseEntity.ok("Place saved successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/fuzzySearch")
    public ResponseEntity<List<Map<String, Object>>> fuzzySearch(@RequestParam("query") String query) throws IOException {
        return ResponseEntity.ok(openSearchService.fuzzySearchByPlaceName(query));
    }

}