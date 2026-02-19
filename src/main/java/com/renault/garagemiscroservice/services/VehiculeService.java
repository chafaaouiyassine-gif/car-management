package com.renault.garagemiscroservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;

import java.util.List;

public interface VehiculeService {

    VehiculeDto createVehicule(VehiculeDto vehiculeDto) throws ArgumentNotValidException, EntityNotFoundException, JsonProcessingException, MaxVehiculeExceedException;
    void updateVehicule(VehiculeDto vehiculeDto) throws ArgumentNotValidException, EntityNotFoundException;
    void deleteVehicule(Integer id) throws ArgumentNotValidException, EntityNotFoundException;
    List<VehiculeDto> getAllVehiculesByGarage(Integer id) throws ArgumentNotValidException, EntityNotFoundException;
    List<VehiculeDto> getAllVehiculesByModele(String modele) throws ArgumentNotValidException;
}
