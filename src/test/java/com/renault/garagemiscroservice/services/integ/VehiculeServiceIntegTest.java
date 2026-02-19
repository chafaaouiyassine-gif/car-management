package com.renault.garagemiscroservice.services.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import com.renault.garagemiscroservice.services.VehiculeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class VehiculeServiceIntegTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VehiculeRepository vehiculeRepository;

    @Autowired
    VehiculeService vehiculeService;

    @Autowired
    VehiculeMapper vehiculeMapper;



    @Autowired
    GarageMapper garageMapper;

    @Autowired
    GarageRepository garageRepository;

    @Value("classpath:vehicule/list_vehicule_to_save.json")
    private Resource vehiculeResource;
    @Value("classpath:garage/list_garage_to_save.json")
    private Resource garageResource;


    List<VehiculeDto> listVehiculeDTO;

    Garage savedGarage;

    @BeforeAll
    void beforeAll() throws IOException {
        List<GarageDto> listGarageDtoToSave = objectMapper.readValue(garageResource.getInputStream(), new TypeReference<List<GarageDto>>() {
        });
        savedGarage= garageRepository.save(garageMapper.fromDto(listGarageDtoToSave.getFirst()));
    }

    @BeforeEach
    void setUp() throws IOException {
        listVehiculeDTO = objectMapper.readValue(vehiculeResource.getInputStream(), new TypeReference<List<VehiculeDto>>() {
        });
    }

    @Test
    void vehicule_create_success() throws Exception {
        VehiculeDto vehiculeDtoToSave=listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        VehiculeDto savedVehicule=vehiculeService.createVehicule(vehiculeDtoToSave);
        assertNotNull(savedVehicule.getId());
        Optional<Vehicule> vehicule= vehiculeRepository.findById(savedVehicule.getId());
        assert vehicule.isPresent();
        assertEquals("2022",vehicule.get().getModel());
    }
    @Test
    void vehicule_create_garage_null()  {
        VehiculeDto vehiculeDtoToSave=listVehiculeDTO.getFirst();
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.createVehicule(vehiculeDtoToSave);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void vehicule_create__not_exists()  {
        VehiculeDto vehiculeDtoToSave=listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(GarageDto.builder().id(74521).build());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehiculeService.createVehicule(vehiculeDtoToSave);
        });
        assertEquals("Entity not found", exception.getMessage());
    }
    @Test
    void vehicule_create_garage_max_vehicules()  {
        savedGarage.setCountVehicule(50);
        garageRepository.save(savedGarage);
        VehiculeDto vehiculeDtoToSave=listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        MaxVehiculeExceedException exception = assertThrows(MaxVehiculeExceedException.class, () -> {
            vehiculeService.createVehicule(vehiculeDtoToSave);
        });
        assertEquals("Max vehicules", exception.getMessage());
        savedGarage.setCountVehicule(0);
        garageRepository.save(savedGarage);
    }
    @Test
    void update_vehicule_success() throws Exception {
        VehiculeDto vehiculeToUpdate=listVehiculeDTO.getFirst();
        vehiculeToUpdate.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehicule=vehiculeRepository.save(vehiculeMapper.fromDto(vehiculeToUpdate));
        assertNotNull(vehicule.getVehiculeId());
        vehiculeToUpdate.setId(vehicule.getVehiculeId());
        vehiculeToUpdate.setBrand("Test 1");
        vehiculeService.updateVehicule(vehiculeToUpdate);
        Optional<Vehicule> savedVehicule= vehiculeRepository.findById(vehicule.getVehiculeId());
        assertTrue(savedVehicule.isPresent());
        assertEquals("Test 1",savedVehicule.get().getBrand());
    }
    @Test
    void update_vehicule_when_null() {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.updateVehicule(null);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void update_vehicule_when_id_null() {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.updateVehicule(VehiculeDto.builder().build());
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void delete_vehicule_when_id_null() {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.deleteVehicule(null);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void delete_vehicule_when_id_not_exists()  {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehiculeService.deleteVehicule(58425);
        });
        assertEquals("Entity not found", exception.getMessage());
    }
    @Test
    void delete_vehicule_success() throws Exception {
        VehiculeDto vehiculeToUpdate=listVehiculeDTO.getFirst();
        vehiculeToUpdate.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehicule=vehiculeRepository.save(vehiculeMapper.fromDto(vehiculeToUpdate));
        vehiculeService.deleteVehicule(vehicule.getVehiculeId());
        Optional<Vehicule> savedVahicule=vehiculeRepository.findById(vehicule.getVehiculeId());
        assertTrue(savedVahicule.isEmpty());

    }
    @Test
    void get_vehicule_by_garageId_null()  {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.getAllVehiculesByGarage(null);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void get_vehicule_by_garage_not_exists()  {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehiculeService.getAllVehiculesByGarage(5846);
        });
        assertEquals("Entity not found", exception.getMessage());
    }
    @Test
    void get_vehicule_by_garage_success() throws ArgumentNotValidException, EntityNotFoundException {
        vehiculeRepository.deleteAll();
        listVehiculeDTO.stream().map(vehiculeMapper::fromDto).forEach(vehicule -> {
            vehicule.setGarage(savedGarage);
            vehiculeRepository.save(vehicule);
        });
       List<VehiculeDto> vehicules= vehiculeService.getAllVehiculesByGarage(savedGarage.getGarageId());
       assertEquals(listVehiculeDTO.size(),vehicules.size());
    }
    @Test
    void get_vehicule_by_model_null()  {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            vehiculeService.getAllVehiculesByModele(null);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void get_vehicule_by_model_success() throws ArgumentNotValidException {
        vehiculeRepository.deleteAll();
        listVehiculeDTO.stream().map(vehiculeMapper::fromDto).forEach(vehicule -> {
            vehicule.setGarage(savedGarage);
            vehiculeRepository.save(vehicule);
        });
        List<VehiculeDto> vehicules= vehiculeService.getAllVehiculesByModele("2022");
        assertEquals(1,vehicules.size());
    }
}