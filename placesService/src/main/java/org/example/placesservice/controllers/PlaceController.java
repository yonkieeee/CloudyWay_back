package org.example.placesservice.controllers;

import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.models.PlaceModel;
import org.example.placesservice.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlace(@RequestParam String placeName) {
        try {
            List<PlaceModel> places = placeService.getPlaceByPlaceName(placeName);
            List<PlaceDTO> response = places.stream()
                    .map(placeModel -> new PlaceDTO(placeModel.getPlaceName(),
                            placeModel.getCity(),
                            placeModel.getCounty(),
                            placeModel.getStreet(),
                            placeModel.getHouseNumber(),
                            placeModel.getCoordinates()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<PlaceDTO>> getCities(@PathVariable String city) {
        try {
            List<PlaceModel> places = placeService.getAllPlacesByCity(city);
            List<PlaceDTO> response = places.stream()
                    .map(placeModel -> new PlaceDTO(placeModel.getPlaceName(),
                            placeModel.getCity(),
                            placeModel.getCounty(),
                            placeModel.getStreet(),
                            placeModel.getHouseNumber(),
                            placeModel.getCoordinates()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllPlaces")
    public ResponseEntity<List<PlaceDTO>> getAllCities() {
        try {
            List<PlaceModel> places = placeService.getAllPlaces();
            List<PlaceDTO> response = places.stream()
                    .map(placeModel -> new PlaceDTO(placeModel.getPlaceName(),
                            placeModel.getCity(),
                            placeModel.getCounty(),
                            placeModel.getStreet(),
                            placeModel.getHouseNumber(),
                            placeModel.getCoordinates()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/addPlace")
    public ResponseEntity<String> createPlace(@RequestBody PlaceModel place) {
        try {
            placeService.save(place);
            return ResponseEntity.ok("Place saved successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}