package com.renault.garagemiscroservice.repositories;

import com.renault.garagemiscroservice.entities.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageRepository extends JpaRepository<Garage,Long> {
}
