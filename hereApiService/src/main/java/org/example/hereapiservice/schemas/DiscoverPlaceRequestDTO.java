package org.example.hereapiservice.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscoverPlaceRequestDTO {
    @JsonProperty("placeName")
    private String placeName;

    @JsonProperty("languageCode")
    private String languageCode;

}
