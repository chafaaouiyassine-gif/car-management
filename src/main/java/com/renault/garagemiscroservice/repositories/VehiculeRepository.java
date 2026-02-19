package com.renault.garagemiscroservice.repositories;

import com.renault.garagemiscroservice.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule,Integer> {
    @Query("SELECT vehicule FROM Vehicule vehicule WHERE vehicule.garage.garageId= :idGarage")
    Optional<List<Vehicule>> findVehiculeByGarageId(@Param("idGarage") Integer id);

    List<Vehicule> findByModel(String model);
}
