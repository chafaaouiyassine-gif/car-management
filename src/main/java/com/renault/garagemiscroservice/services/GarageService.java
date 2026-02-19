package com.renault.garagemiscroservice.services;

import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GarageService {
    GarageDto saveGarage(GarageDto garage) throws ArgumentNotValidException;
    void updateGarage(GarageDto garage) throws ArgumentNotValidException, EntityNotFoundException;
    GarageDto getGarageById(Integer id) throws  EntityNotFoundException;
    void deleteGarage(Integer id) throws ArgumentNotValidException, EntityNotFoundException;
    List<GarageDto> getAllGarageSorted(Pageable pageable);
    List<GarageDto> getAllGarageByTypeVehiculeORAccessoire(TypeVehicule type,Integer accessoireId) throws ArgumentNotValidException;
}
