package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.utils.GenerateValueToTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
class GarageControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageMapper garageMapper;

    /**
     * check if the creation will be good if the object Garage is correct
     * @throws Exception
     */
    @Test
    void create_garage_success() throws Exception {
        String garageJson=objectMapper.writeValueAsString(GenerateValueToTest.generateGarageDtoForCreate());
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isCreated());
    }

    /**
     * check if @valid works correctly and we cannot create the object
     * @throws Exception
     */
    @Test
    void create_garage_not_valid_data() throws Exception {
        String garageJson=objectMapper.writeValueAsString(GenerateValueToTest.generateGarageDtoForCreateNotValideEmail());
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isBadRequest());
    }

    /**
     * check if the garage is successfully added to DB
     * @throws Exception
     */
    @Test
    @Transactional
    void create_garage_check_add_to_db() throws Exception {
        String garageJson=objectMapper.writeValueAsString(GenerateValueToTest.generateGarageDtoForCreate());
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isCreated());
        assertEquals(1,garageRepository.count());
    }

    /**
     * check if the creation is successfully when the garage is correct
     * @throws Exception
     */
    @Test
    @Transactional
    void update_garage_success() throws Exception {
       Garage garage= garageRepository.save(garageMapper.fromDto(GenerateValueToTest.generateGarageDtoForCreate()));
        String garageJson=objectMapper.writeValueAsString(garageMapper.toDto(garage));
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isOk());
    }

    /**
     * check that when we don't have a object in db we will get exception bad_request
     * @throws Exception
     */
    @Test
    void update_garage_not_existing() throws Exception {
        String garageJson=objectMapper.writeValueAsString(GenerateValueToTest.generateGarageDtoForUpdate());
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isBadRequest());
    }

    /**
     * try to check if @valid works fine
     * @throws Exception
     */
    @Test
    void update_garage_data_not_valid() throws Exception {
        String garageJson=objectMapper.writeValueAsString(GenerateValueToTest.generateGarageDtoForUpdateNotValideAddress());
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageJson)).andExpect(status().isBadRequest());
    }

    /**
     * try to delete object when we get a correct id
     * @throws Exception
     */
    @Test
    void delete_garage_success() throws Exception {
        garageRepository.save(garageMapper.fromDto(GenerateValueToTest.generateGarageDtoForCreate()));
        mockMvc.perform(delete("/garage/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    /**
     * check that @notnull validation works
     * @throws Exception
     */
    @Test
    void delete_garage_id_not_valid() throws Exception {
        garageRepository.save(garageMapper.fromDto(GenerateValueToTest.generateGarageDtoForCreate()));
        mockMvc.perform(delete("/garage/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void get_all_garages_success() throws Exception {
       GenerateValueToTest.generateMultiGarageDtoForCreate()
                .stream().map(garageMapper::fromDto).forEach(garageRepository::save);
       // check the sort by name
        MvcResult mvcResult=mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=name&direction=DESC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        List<GarageDto> garagesList=objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GarageDto>>() {});
       assertEquals("zibola", garagesList.get(0).getName());
        assertEquals("bilora", garagesList.get(1).getName());
        assertEquals("abisco", garagesList.get(2).getName());
        // check the sort by email
        MvcResult mvcResultEmail=mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=email&direction=DESC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
         garagesList=objectMapper.readValue(mvcResultEmail.getResponse().getContentAsString(), new TypeReference<List<GarageDto>>() {});
        assertEquals("stephane@gmail.com", garagesList.get(0).getEmail());
        assertEquals("nadia@gmail.com", garagesList.get(1).getEmail());
        assertEquals("batsite@gmail.com", garagesList.get(2).getEmail());
    }

    @AfterEach
    void tearDown() {
        garageRepository.deleteAll();
    }
}