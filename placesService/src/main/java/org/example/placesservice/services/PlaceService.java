package org.example.placesservice.services;

import org.example.placesservice.models.PlaceModel;
import org.example.placesservice.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceModel> getPlaceByPlaceName(String placeName) {
        return placeRepository.findByPlaceName(placeName);
    }

    public List<PlaceModel> getAllPlacesByCity(String city) {
        return placeRepository.findAllByCity(city);
    }

    public void save(PlaceModel place) {
        placeRepository.save(place);
    }

    public List<PlaceModel> getAllPlaces() {
        return placeRepository.findAll();
    }
}
