package com.renault.garagemiscroservice.repositories;

import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage,Integer> {

    @Query("SELECT DISTINCT vehicule.garage FROM Vehicule vehicule LEFT JOIN vehicule.accessoires accessoire " +
            "WHERE accessoire.accessoireId=:accessoire OR vehicule.typeVehicule=:type")
    Optional<List<Garage>> findGarageByTypeVehiculeAndAccessoire(@Param("type") TypeVehicule type, @Param("accessoire") Integer accessoireId);
}
