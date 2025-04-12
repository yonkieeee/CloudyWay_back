package org.example.placesservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "places")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @JsonProperty("here_api_id")
    private String here_api_id;

    @Column(nullable = false)
    @JsonProperty("placeName")
    private String placeName;

    @Column(nullable = false)
    @JsonProperty("city")
    private String city;

    @Column(nullable = false)
    @JsonProperty("county")
    private String county;

    @Column(nullable = false)
    @JsonProperty("street")
    private String street;

    @Column(name = "house_number")
    @JsonProperty("houseNumber")
    private String houseNumber;

    @Type(JsonBinaryType.class)
    @JsonProperty("coordinates")
    @Column(columnDefinition = "jsonb")
    private CoordinatesModel coordinates;

    @Column(nullable = true)
    @JsonProperty("description")
    private String description;

    @Column(nullable = true)
    @JsonProperty("photo")
    private String photo;
}

