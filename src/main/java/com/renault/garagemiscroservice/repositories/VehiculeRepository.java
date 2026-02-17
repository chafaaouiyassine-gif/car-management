package com.renault.garagemiscroservice.repositories;

import com.renault.garagemiscroservice.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {
    @Query("SELECT vehicule FROM Vehicule vehicule WHERE vehicule.garage.garageId= :idGarage")
    List<Vehicule> findVehiculeByGarageId(@Param("idGarage") Long id);

    List<Vehicule> findByModel(String model);
}
