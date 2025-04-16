package org.example.placesservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.mappers.PlaceMapper;
import org.example.placesservice.models.PlaceModel;
import org.example.placesservice.repositories.PlaceRepository;
import org.example.placesservice.services.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Override
    public List<PlaceDTO> getPlaceByPlaceName(String placeName) {
        return placeRepository.findByPlaceName(placeName).stream()
                .map(placeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDTO> getAllPlacesByCity(String city) {
        return placeRepository.findAllByCity(city).stream()
                .map(placeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(PlaceModel place) {
        placeRepository.save(place);
    }

    @Override
    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(placeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
