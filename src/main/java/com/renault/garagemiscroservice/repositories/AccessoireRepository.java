package com.renault.garagemiscroservice.repositories;

import com.renault.garagemiscroservice.entities.Accessoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessoireRepository extends JpaRepository<Accessoire,Long> {
    @Query("SELECT accessoire FROM Accessoire accessoire WHERE accessoire.vehicule.vehiculeId= :idVehicule")
    List<Accessoire> findAccessoireByVehiculeId(@Param("idVehicule") Long id);
}
