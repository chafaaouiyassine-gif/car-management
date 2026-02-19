package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Accessoire;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.mappers.AccessoireMapper;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.repositories.AccessoireRepository;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccessoireControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VehiculeRepository vehiculeRepository;

    @Autowired
    AccessoireRepository accessoireRepository;

    @Value("classpath:garage/list_garage_to_save.json")
    private Resource garageResource;
    @Value("classpath:vehicule/list_vehicule_to_save.json")
    private Resource vehiculeResource;
    @Value("classpath:accessoire/list_accessoires_to_save.json")
    private Resource accessoireResource;

    @Value("${vehicule.max}")
    private int maxVihicule;

    @Autowired
    private GarageMapper garageMapper;
    @Autowired
    private VehiculeMapper vehiculeMapper;

    List<AccessoireDTO> listAccessoireToSave;
    Vehicule vehiculeReferencedToCreateAccessoire;
    @Autowired
    private AccessoireMapper accessoireMapper;
    @BeforeAll
    void saveVahiculeAndGarage() throws IOException {
        List<GarageDto> listGarageDtoToSave = objectMapper.readValue(garageResource.getInputStream(), new TypeReference<List<GarageDto>>() {
        });
        List<VehiculeDto> listVehiculeToSave = objectMapper.readValue(vehiculeResource.getInputStream(), new TypeReference<List<VehiculeDto>>() {
        });
        listAccessoireToSave = objectMapper.readValue(accessoireResource.getInputStream(), new TypeReference<List<AccessoireDTO>>() {
        });
        Garage garageReferencedToCreateVehicule = garageRepository.save(garageMapper.fromDto(listGarageDtoToSave.getFirst()));
        if (garageReferencedToCreateVehicule.getCountVehicule() < maxVihicule) {
            garageReferencedToCreateVehicule.setCountVehicule(garageReferencedToCreateVehicule.getCountVehicule() + 1);
            garageRepository.save(garageReferencedToCreateVehicule);
            vehiculeReferencedToCreateAccessoire = vehiculeMapper.fromDto(listVehiculeToSave.getFirst());
            vehiculeReferencedToCreateAccessoire.setGarage(garageReferencedToCreateVehicule);
            vehiculeRepository.save(vehiculeReferencedToCreateAccessoire);
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        listAccessoireToSave = objectMapper.readValue(accessoireResource.getInputStream(), new TypeReference<List<AccessoireDTO>>() {
        });
    }
    @Test
    void create_accessoire_success() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        String accessoireJson = objectMapper.writeValueAsString(accessoireDTOToSave);
        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON).content(accessoireJson)).andExpect(status().isCreated());
    }

    @Test
    void create_accessoire_when_vehicule_not_existing() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(VehiculeDto.builder().id(14585695).build());
        String accessoireJson =objectMapper.writeValueAsString(accessoireDTOToSave);
        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON)
                .content(accessoireJson))
                .andExpect(status().isNotFound());
    }
    @Test
    void create_accessoire_when_vehicule_null() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        String accessoireJson =objectMapper.writeValueAsString(accessoireDTOToSave);
        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON)
                .content(accessoireJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_accessoire_when_data_not_valid() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        accessoireDTOToSave.setDescription(null);
        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessoireDTOToSave)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void update_accessoire_success() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        Accessoire accessoireToSaveInRepository =accessoireMapper.fromDto(accessoireDTOToSave);
        accessoireRepository.save(accessoireToSaveInRepository);
        accessoireDTOToSave.setId(accessoireToSaveInRepository.getAccessoireId());
        accessoireDTOToSave.setNom("test48");
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessoireDTOToSave)))
                .andExpect(status().isOk());
        Optional<Accessoire> accessoireSaved = accessoireRepository.findById(accessoireDTOToSave.getId());
        assert accessoireSaved.isPresent();
        assertEquals("test48", accessoireSaved.get().getNom());
    }

    @Test
    void update_accessoire_data_not_valid() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        Accessoire accessoireToSaveInRepository =accessoireMapper.fromDto(accessoireDTOToSave);
        accessoireRepository.save(accessoireToSaveInRepository);
        accessoireDTOToSave.setId(accessoireToSaveInRepository.getAccessoireId());
        accessoireDTOToSave.setNom("test48");
        accessoireDTOToSave.setDescription(null);
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessoireDTOToSave)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void update_accessoire_not_exesting() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessoireDTOToSave)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void update_accessoire_vahicule_not_existing() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        Accessoire accessoireToSaveInRepository =accessoireMapper.fromDto(accessoireDTOToSave);
        accessoireRepository.save(accessoireToSaveInRepository);
        accessoireDTOToSave.setId(accessoireToSaveInRepository.getAccessoireId());
        accessoireDTOToSave.setNom("test48");
        accessoireDTOToSave.setVehicule(VehiculeDto.builder().id(145885215).build());
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessoireDTOToSave)))
                .andExpect(status().isNotFound());

    }

    @Test
    void delete_accessoire_success() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        Accessoire accessoire = accessoireMapper.fromDto(accessoireDTOToSave);
        accessoireRepository.save(accessoire);
        mockMvc.perform(delete("/accessoire/v1/delete?id="+accessoire.getAccessoireId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    void delete_accessoire_id_empty() throws Exception {
        AccessoireDTO accessoireDTOToSave=listAccessoireToSave.getFirst();
        accessoireDTOToSave.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
        Accessoire accessoire = accessoireMapper.fromDto(accessoireDTOToSave);
        accessoireRepository.save(accessoire);
        mockMvc.perform(delete("/accessoire/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }
    @Test
    void delete_accessoire_id_not_existing() throws Exception {
        mockMvc.perform(delete("/accessoire/v1/delete?id=85471258").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void get_accessoire_by_vehicule() throws Exception {
        accessoireRepository.deleteAll();
        for(AccessoireDTO accessoireDTO:listAccessoireToSave){
            accessoireDTO.setVehicule(vehiculeMapper.toDto(vehiculeReferencedToCreateAccessoire));
            Accessoire accessoire = accessoireMapper.fromDto(accessoireDTO);
            accessoireRepository.save(accessoire);
        }
        MvcResult mvcResult = mockMvc.perform(get("/accessoire/v1/by_vehicule?idVehicule="+vehiculeReferencedToCreateAccessoire.getVehiculeId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        List<AccessoireDTO> garagesList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(listAccessoireToSave.size(), garagesList.size());

    }
}
