package org.example.placesservice.services;

import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.models.PlaceModel;

import java.util.List;

public interface PlaceService {

    List<PlaceDTO> getPlaceByPlaceName(String placeName);
    List<PlaceDTO> getAllPlacesByCity(String city);
    void save(PlaceModel place);
    List<PlaceDTO> getAllPlaces();
}
