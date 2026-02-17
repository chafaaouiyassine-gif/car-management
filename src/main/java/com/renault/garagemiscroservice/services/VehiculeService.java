package com.renault.garagemiscroservice.services;

import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;

import java.util.List;

public interface VehiculeService {

     void createVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException;
    void updateVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException;
    void deleteVehicule(Long id) throws MethodArgumentNotValidException, EntityNotFoundException;
    List<VehiculeDto> getAllVehiculesByGarage(Long id) throws MethodArgumentNotValidException;
    List<VehiculeDto> getAllVehiculesByModele(String modele) throws MethodArgumentNotValidException;
}
