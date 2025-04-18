package org.example.hereapiservice.dto;

import java.util.Map;

public record DiscoverPlaceResponseDTO(String placeName, String hereApiId, String city, String county, String street, String houseNumber, Map<String, String> coordinates) {
}
