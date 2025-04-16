package org.example.placesservice.mappers;

import org.example.placesservice.dto.PlaceDTO;
import org.example.placesservice.models.PlaceModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    PlaceDTO toDTO(PlaceModel placeModel);
}
