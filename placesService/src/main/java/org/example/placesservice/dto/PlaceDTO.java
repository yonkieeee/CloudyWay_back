package org.example.placesservice.dto;

import org.example.placesservice.models.CoordinatesModel;

public record PlaceDTO(String placeName, String city, String county, String street, String houseNumber, CoordinatesModel coordinates) {}
