package com.renault.garagemiscroservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;

import java.util.List;

public interface VehiculeService {

     void createVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException, JsonProcessingException;
    void updateVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException;
    void deleteVehicule(Integer id) throws MethodArgumentNotValidException, EntityNotFoundException;
    List<VehiculeDto> getAllVehiculesByGarage(Integer id) throws MethodArgumentNotValidException;
    List<VehiculeDto> getAllVehiculesByModele(String modele) throws MethodArgumentNotValidException;
}
