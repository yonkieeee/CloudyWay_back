package org.example.placesservice.repositories;

import org.example.placesservice.models.PlaceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceModel, Long>{
    List<PlaceModel> findByPlaceName(String name);
    List<PlaceModel> findAllByCity(String city);
}
