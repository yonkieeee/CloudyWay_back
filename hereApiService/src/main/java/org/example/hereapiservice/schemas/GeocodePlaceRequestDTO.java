package org.example.hereapiservice.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodePlaceRequestDTO {
    @JsonProperty("placeName")
    private String placeName;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;
}
