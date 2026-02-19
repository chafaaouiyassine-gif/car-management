package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehiculeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VehiculeRepository vehiculeRepository;

    @Value("classpath:garage/list_garage_to_save.json")
    private Resource garageResource;
    @Value("classpath:vehicule/list_vehicule_to_save.json")
    private Resource vehiculeResource;

    Garage savedGarage;
    List<VehiculeDto> listVehiculeDTO;
    @Autowired
    private GarageMapper garageMapper;
    @Autowired
    private VehiculeMapper vehiculeMapper;

    @BeforeAll
    @Transactional
    void saveGarage() throws IOException {
        List<GarageDto> listGarageDtoToSave = objectMapper.readValue(garageResource.getInputStream(), new TypeReference<List<GarageDto>>() {
        });
        savedGarage = garageRepository.save(garageMapper.fromDto(listGarageDtoToSave.getFirst()));
    }

    @BeforeEach
    void setUp() throws IOException {
        listVehiculeDTO = objectMapper.readValue(vehiculeResource.getInputStream(), new TypeReference<List<VehiculeDto>>() {
        });
    }


    @Test
    void create_vehicule_success() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_vehicule_data_not_valid() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        vehiculeDtoToSave.setBrand(null);
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_vehicule_garage_not_exists() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(GarageDto.builder().id(15884812).build());
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_vehicule_garage_null() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_vehicule_success() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehiculeToSave = vehiculeMapper.fromDto(vehiculeDtoToSave);
        vehiculeRepository.save(vehiculeToSave);
        assertEquals(TypeVehicule.CAMION, vehiculeDtoToSave.getTypeVehicule());
        vehiculeDtoToSave.setId(vehiculeToSave.getVehiculeId());
        vehiculeDtoToSave.setTypeVehicule(TypeVehicule.VOITURE);
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isOk());
        Optional<Vehicule> savedVehicule = vehiculeRepository.findById(vehiculeToSave.getVehiculeId());
        assertTrue(savedVehicule.isPresent());
        assertEquals(vehiculeDtoToSave.getTypeVehicule(), savedVehicule.get().getTypeVehicule());
    }

    @Test
    void update_vehicule_garage_not_existing() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehiculeToSave = vehiculeMapper.fromDto(vehiculeDtoToSave);
        vehiculeRepository.save(vehiculeToSave);
        vehiculeDtoToSave.setId(vehiculeToSave.getVehiculeId());
        vehiculeDtoToSave.setBrand("BYD");
        vehiculeDtoToSave.setGarage(GarageDto.builder().id(552545).build());
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isNotFound());

    }

    @Test
    void update_vehicule_data_not_valide() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehiculeToSave = vehiculeMapper.fromDto(vehiculeDtoToSave);
        vehiculeRepository.save(vehiculeToSave);
        vehiculeDtoToSave.setId(vehiculeToSave.getVehiculeId());
        vehiculeDtoToSave.setBrand(null);
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculeDtoToSave)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void delete_vehicule_success() throws Exception {
        VehiculeDto vehiculeDtoToSave = listVehiculeDTO.getFirst();
        vehiculeDtoToSave.setGarage(garageMapper.toDto(savedGarage));
        Vehicule vehiculeToSave = vehiculeMapper.fromDto(vehiculeDtoToSave);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(delete("/vehicule/v1/delete?id=" + vehiculeToSave.getVehiculeId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Optional<Vehicule> savedVehicule=vehiculeRepository.findById(vehiculeToSave.getVehiculeId());
        assertTrue(savedVehicule.isEmpty());
    }

    @Test
    void delete_vehicule_absent_vehicule() throws Exception {
        mockMvc.perform(delete("/vehicule/v1/delete?id=84758987").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void delete_vehicule_id_empty() throws Exception {
        mockMvc.perform(delete("/vehicule/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void get_vehicule_by_garage() throws Exception {
        vehiculeRepository.deleteAll();
        listVehiculeDTO.stream().map(vehiculeMapper::fromDto).forEach(vehicule -> {
            vehicule.setGarage(savedGarage);
            vehiculeRepository.save(vehicule);
        });
        MvcResult mvcResult = mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=" + savedGarage.getGarageId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        List<VehiculeDto> vehiculeList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(listVehiculeDTO.size(), vehiculeList.size());
    }

    @Test
    void get_vehicule_by_missing_garage() throws Exception {
        mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=874596").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}