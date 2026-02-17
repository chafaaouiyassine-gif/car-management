package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.entities.Accessoire;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.repositories.AccessoireRepository;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    AccessoireRepository  accessoireRepository;


    @BeforeAll
    @Transactional
    void saveVahiculeAndGarage() throws JsonProcessingException {
        String garageJson="{" +
                "    \"id\": null," +
                "    \"name\":\"adino\"," +
                "    \"address\":{" +
                "              \"id\":null," +
                "              \"numero\":15," +
                "              \"rue\":\"jabal kotama\"," +
                "              \"ville\":\"Settat\"," +
                "              \"pays\":\"Maroc\"" +
                "               },\n" +
                "    \"telephone\":\"0668790439\"," +
                "    \"email\":\"bodino@gmail.com\"," +
                "    \"horaireOvertureList\":[" +
                "                               {" +
                "                                 \"horaireOuvertureId\":null," +
                "                                 \"dayOfWeek\":\"LUNDI\"," +
                "                                 \"openingTimeList\":[" +
                "                                                        {" +
                "                                                            \"openingTimeId\":null," +
                "                                                            \"startyTime\":\"08:30:00\"," +
                "                                                            \"endTime\":\"17:30:00\"" +

                "                                                         }" +
                "                                                   ]" +

                "                                }" +

                "                           ]" +
                "}";
        String vehiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
       Garage garageTocheck= garageRepository.save(objectMapper.readValue(garageJson, Garage.class));
        Garage garage=garageRepository.findById(garageTocheck.getGarageId()).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vehiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
    }
    @Test
    void create_accessoire_success() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 123\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";

        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON).content(accessoireJson)).andExpect(status().isCreated());

    }
    @Test
    void create_accessoire_when_vehicule_not_existing() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 123\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"12\"" +
                "    }" +
                "}";

        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON).content(accessoireJson)).andExpect(status().isNotFound());

    }
    @Test
    void create_accessoire_when_data_not_valid() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":null," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";

        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON).content(accessoireJson)).andExpect(status().isBadRequest());

    }
    @Test
    void update_accessoire_success() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 123\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);
        String accessoireJsonToUpdate="{" +
                "    \"id\":" +accessoire.getAccessoireId()+", "+
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test48\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON).content(accessoireJsonToUpdate)).andExpect(status().isOk());

        Optional<Accessoire> accessoireSaved=accessoireRepository.findById(accessoire.getAccessoireId());
        assert accessoireSaved.isPresent();
        assertEquals("test48",accessoireSaved.get().getDescription());
    }
    @Test
    void update_accessoire_data_not_valid() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 123\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);
        String accessoireJsonToUpdate="{" +
                "    \"id\":1," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":null," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON).content(accessoireJsonToUpdate)).andExpect(status().isBadRequest());

    }
    @Test
    void update_accessoire_vahicule_not_existing() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 123\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);
        String accessoireJsonToUpdate="{" +
                "    \"id\":1," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"test 777\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"18\"" +
                "    }" +
                "}";
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON).content(accessoireJsonToUpdate)).andExpect(status().isNotFound());

    }
    @Test
    void delete_accessoire_success() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":null," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);
        mockMvc.perform(delete("/accessoire/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }
    @Test
    void delete_accessoire_id_empty() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":null," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);
        mockMvc.perform(delete("/accessoire/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }
    @Test
    void get_accessoire_by_vehicule() throws Exception {
        String accessoireJson="{" +
                "    \"id\":null," +
                "    \"nom\":\"turbo\"," +
                "    \"description\":\"description 123456\"," +
                "    \"prix\":152.25,\n" +
                "    \"type\":\"type 1\"," +
                "    \"vehicule\":{" +
                "        \"id\":\"1\"" +
                "    }" +
                "}";
        accessoireRepository.deleteAll();
        Vehicule vehicule=vehiculeRepository.findAll().getFirst();
        Accessoire accessoire=objectMapper.readValue(accessoireJson,Accessoire.class);
        accessoire.setVehicule(vehicule);
        for(int i=0;i<3;i++){
            accessoire.setAccessoireId(null);
            accessoireRepository.save(accessoire);
        }
        long totalRaws=accessoireRepository.count();
        MvcResult mvcResult=   mockMvc.perform(get("/accessoire/v1/by_vehicule?idVehicule=1").contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk()).andReturn();
        List<AccessoireDTO> garagesList=objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(totalRaws,garagesList.size());

    }
}
