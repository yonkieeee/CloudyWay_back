package org.example.hereapiservice.mappers;

import org.example.hereapiservice.dto.DiscoverPlaceResponseDTO;
import org.example.hereapiservice.dto.PlaceResponseDTO;
import org.example.hereapiservice.dto.PositionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface DiscoverMapper {

    @Mapping(source = "title", target = "placeName")
    @Mapping(source = "id", target = "hereApiId")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.county", target = "county")
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.houseNumber", target = "houseNumber")
    @Mapping(source = "position", target = "coordinates")
    DiscoverPlaceResponseDTO toDiscoverPlaceResponseDTO(PlaceResponseDTO placeResponseDTO);

    List<DiscoverPlaceResponseDTO> toListDiscoverPlaceResponseDTO(List<PlaceResponseDTO> placeResponseDTOList);

    default Map<String, String> mapPositionToCoordinates(PositionsDTO position) {
        if (position == null) {
            return null;
        }
        return Map.of(
                "lat", String.valueOf(position.getLat()),
                "lng", String.valueOf(position.getLng())
        );
    }
}
