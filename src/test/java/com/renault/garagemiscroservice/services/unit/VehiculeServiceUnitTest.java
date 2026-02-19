package com.renault.garagemiscroservice.services.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.producers.VehiculeProducer;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import com.renault.garagemiscroservice.service_imp.VehiculeServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.SendResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.renault.garagemiscroservice.utils.DtoAndEntitiesGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceUnitTest {
    @Mock
    VehiculeRepository vehiculeRepository;
    @Mock
    GarageRepository garageRepository;
    @Mock
    VehiculeMapper vehiculeMapper;
    @Mock
    VehiculeProducer vehiculeProducer;

    @InjectMocks
    VehiculeServiceImp vehiculeService;

    VehiculeDto vehiculeDto ;
    Vehicule vehiculeEntityToSave ;
    Vehicule vehiculeEntity ;

    @BeforeEach
    void setUp()  {
        vehiculeDto = generateVehiculeDto();
        vehiculeEntityToSave = generateVehiculeEntity();
        vehiculeEntity = generateVehiculeEntity();
        vehiculeService.setMaxVehicul(50);
    }

    @Test
    void create_vehicule_success() throws ArgumentNotValidException, MaxVehiculeExceedException, EntityNotFoundException, JsonProcessingException {
        vehiculeEntity.setVehiculeId(1);
        when(vehiculeMapper.fromDto(any(VehiculeDto.class))).thenReturn(vehiculeEntityToSave);
        when(vehiculeMapper.toDto(any(Vehicule.class))).thenReturn(vehiculeDto);
        when(vehiculeRepository.save(any(Vehicule.class))).thenReturn(vehiculeEntity);
        when(garageRepository.findById(any(Integer.class))).thenReturn(Optional.of(Garage.builder().garageId(1).build()));
        when(vehiculeProducer.sendVehicule(any(Vehicule.class))).thenReturn(new CompletableFuture<SendResult<Integer, String>>());
        VehiculeDto savedVehicule = vehiculeService.createVehicule(vehiculeDto);
        assertNotNull(savedVehicule);
    }
    @Test
    void create_vehicule_garage_null()  {
        vehiculeDto.setGarage(null);
        ArgumentNotValidException ex=assertThrows(ArgumentNotValidException.class,()->{
            vehiculeService.createVehicule(vehiculeDto);
        });
        assertEquals("Argument not valid",ex.getMessage());
    }
}
