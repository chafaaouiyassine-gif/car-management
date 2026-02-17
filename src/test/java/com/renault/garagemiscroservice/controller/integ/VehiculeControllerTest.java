package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeAll
    @Transactional
    void saveGarage() throws JsonProcessingException {
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
                "                                                            \"startyTime\":\"2022-06-15\"," +
                "                                                            \"endTime\":\"2022-06-16\"" +

                "                                                         }" +
                "                                                   ]" +

                "                                }" +

                "                           ]" +
                "}";
        garageRepository.save(objectMapper.readValue(garageJson, Garage.class));
    }



    @Test
    void create_vehicule_success() throws Exception {

        String vahiculeString="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON).content(vahiculeString)).andExpect(status().isCreated());
    }
    @Test
    void create_vehicule_data_not_valid() throws Exception {
        String vahiculeString="{" +
                "    \"id\":null," +
                "    \"brand\":null," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON).content(vahiculeString)).andExpect(status().isBadRequest());
    }
    @Test
    void create_vehicule_data_not_garage() throws Exception {
        String vahiculeString="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":5" +
                "    }" +
                "}";
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON).content(vahiculeString)).andExpect(status().isBadRequest());
    }
    @Test
    void update_vehicule_success() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        String vahiculeJsonToUpdate="{" +
                "    \"id\":1," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2014\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON).content(vahiculeJsonToUpdate)).andExpect(status().isOk());

    }
    @Test
    void update_vehicule_garage_not_existing() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        String vahiculeJsonToUpdate="{" +
                "    \"id\":1," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2014\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":10" +
                "    }" +
                "}";
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON).content(vahiculeJsonToUpdate)).andExpect(status().isBadRequest());

    }
    @Test
    void update_vehicule_data_not_valide() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        String vahiculeJsonToUpdate="{" +
                "    \"id\":1," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2014\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":null," +
                "    \"garage\":{" +
                "    \"id\":10" +
                "    }" +
                "}";

        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON).content(vahiculeJsonToUpdate)).andExpect(status().isBadRequest());

    }

    @Test
    void delete_vehicule_success() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(delete("/vehicule/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }
    @Test
    void delete_vehicule_absent_vehicule() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(delete("/vehicule/v1/delete?id=1111").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }
    @Test
    void delete_vehicule_data_not_valid() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        mockMvc.perform(delete("/vehicule/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }
    @Test
    void get_vehicule_by_garage() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        vehiculeRepository.deleteAll();
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        for(int i=0;i<3;i++){
            vehiculeToSave.setVehiculeId(null);
            vehiculeRepository.save(vehiculeToSave);
        }
        MvcResult mvcResult=   mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        List<VehiculeDto> garagesList=objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(3,garagesList.size());
    }
    @Test
    void get_vehicule_by_missing_garage() throws Exception {

        String vahiculeJson="{" +
                "    \"id\":null," +
                "    \"brand\":\"Mercedesse\"," +
                "    \"model\":\"2015\"," +
                "    \"anneeFabrication\":\"2012\"," +
                "    \"typeCarburant\":\"DIESEL\"," +
                "    \"garage\":{" +
                "    \"id\":1" +
                "    }" +
                "}";
        vehiculeRepository.deleteAll();
        Garage garage=garageRepository.findById(1L).orElse(null);
        Vehicule vehiculeToSave=objectMapper.readValue(vahiculeJson, Vehicule.class);
        vehiculeToSave.setGarage(garage);
        for(int i=0;i<3;i++){
            vehiculeToSave.setVehiculeId(null);
            vehiculeRepository.save(vehiculeToSave);
        }
        MvcResult mvcResult=   mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=5").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        List<VehiculeDto> garagesList=objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(0,garagesList.size());
    }
}