package com.renault.garagemiscroservice.services;

import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GarageService {
    GarageDto saveGarage(GarageDto garage) throws MethodArgumentNotValidException;
    void updateGarage(GarageDto garage) throws MethodArgumentNotValidException;
    GarageDto getGarageById(Integer id) throws  EntityNotFoundException;
    void deleteGarage(Integer id) throws MethodArgumentNotValidException;
    List<GarageDto> getAllGarageSorted(Pageable pageable);
}
