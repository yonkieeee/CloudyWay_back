package org.example.placesservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesModel {

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lng")
    private Double lng;
}
