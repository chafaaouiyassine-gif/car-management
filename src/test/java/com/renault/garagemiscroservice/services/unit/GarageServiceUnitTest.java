package com.renault.garagemiscroservice.services.unit;


import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.service_imp.GarageServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.renault.garagemiscroservice.utils.DtoAndEntitiesGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GarageServiceUnitTest {


    @Mock
    GarageRepository garageRepository;

    @Mock
    GarageMapper garageMapper;

    @InjectMocks
    GarageServiceImp garageService;
    GarageDto garageDto ;
    Garage garageEntityToSave ;
    Garage garageEntity ;

    @BeforeEach
    void setUp()  {
         garageDto = generateGarageDto();
         garageEntityToSave = generateGarageEntity();
         garageEntity = generateGarageEntity();
    }

    @Test
    void create_garage_success() throws  ArgumentNotValidException {

        garageEntity.setGarageId(1);
        when(garageMapper.fromDto(any(GarageDto.class))).thenReturn(garageEntityToSave);
        when(garageMapper.toDto(any(Garage.class))).thenReturn(garageDto);
        when(garageRepository.save(any(Garage.class))).thenReturn(garageEntity);
        GarageDto garageSaved = garageService.saveGarage(garageDto);
        assertNotNull(garageSaved);
    }
    @Test
    void create_garage_down()  {
        ArgumentNotValidException ex=assertThrows(ArgumentNotValidException.class,()->{
            garageService.saveGarage(null);
        });
        assertEquals("Argument not valid",ex.getMessage());
    }
    @Test
    void update_garage_success() throws ArgumentNotValidException, EntityNotFoundException {
        garageEntity.setGarageId(1);
        garageDto.setId(1);
        garageEntityToSave.setGarageId(1);
        when(garageMapper.fromDto(any(GarageDto.class))).thenReturn(garageEntityToSave);
        when(garageMapper.toDto(any(Garage.class))).thenReturn(garageDto);
        when(garageRepository.save(any(Garage.class))).thenReturn(garageEntity);
        when(garageRepository.findById(any(Integer.class))).thenReturn(Optional.of(Garage.builder().garageId(1).build()));
        garageService.updateGarage(garageDto);
        verify(garageRepository).save(garageEntity);
    }
    @Test
    void update_garage_notExist()  {
        garageDto.setId(1);
        when(garageRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        EntityNotFoundException ex=assertThrows(EntityNotFoundException.class,()->{
            garageService.updateGarage(garageDto);
        });
        assertEquals("Entity not found",ex.getMessage());
    }

}
