package com.renault.garagemiscroservice.controller.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.ObjectUtils.isEmpty;

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

    @Value("classpath:garage/list_garage_to_save.json")
    private Resource garageResource;

    List<GarageDto> listGarageDtoToSave;

    @BeforeEach
    void setUp() throws IOException {
        listGarageDtoToSave = objectMapper.readValue(garageResource.getInputStream(), new TypeReference<List<GarageDto>>() {
        });
    }

    /**
     * check if the creation will be good if the object Garage is correct
     *
     * @throws Exception
     */
    @Test
    void create_garage_success() throws Exception {
        String garageJson = objectMapper.writeValueAsString(listGarageDtoToSave.getFirst());
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(garageJson))
                .andExpect(status().isCreated());
    }

    /**
     * check if @valid works correctly and we cannot create the object
     *
     * @throws Exception
     */
    @Test
    void create_garage_not_valid_data() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        garageDtoToSave.setEmail(null);
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDtoToSave)))
                .andExpect(status().isBadRequest());
    }

    /**
     * check if the garage is successfully added to DB
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void create_garage_check_add_to_db() throws Exception {
        String garageJson = objectMapper.writeValueAsString(listGarageDtoToSave.getFirst());
        MvcResult mvcResult = mockMvc.perform(post("/garage/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(garageJson)).andExpect(status().isCreated()).andReturn();
        GarageDto garageDtoSaved = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GarageDto.class);
        assertNotNull(garageDtoSaved.getId());
        Optional<Garage> garageTocheckWith = garageRepository.findById(garageDtoSaved.getId());
        assertTrue(garageTocheckWith.isPresent());
    }

    /**
     * check if the creation is successfully when the garage is correct
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void update_garage_success() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        Garage garageToSave = garageMapper.fromDto(garageDtoToSave);
        garageRepository.save(garageToSave);
        garageDtoToSave.setId(garageToSave.getGarageId());
        garageDtoToSave.setName("test123");
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(garageDtoToSave)))
                .andExpect(status().isOk());
        Optional<Garage> garageToCheck = garageRepository.findById(garageToSave.getGarageId());
        assertTrue(garageToCheck.isPresent());
        assertEquals(garageDtoToSave.getName(), garageToCheck.get().getName());
    }

    /**
     * check that when we don't have a object in db we will get exception bad_request
     *
     * @throws Exception
     */
    @Test
    void update_garage_not_existing() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        garageDtoToSave.setId(15748526);
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDtoToSave)))
                .andExpect(status().isNotFound());
    }

    /**
     * try to check if @valid works fine
     *
     * @throws Exception
     */
    @Test
    void update_garage_data_not_valid() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        garageDtoToSave.setEmail(null);
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDtoToSave)))
                .andExpect(status().isBadRequest());
    }

    /**
     * try to delete object when we get a correct id
     *
     * @throws Exception
     */
    @Test
    void delete_garage_success() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        Garage garageToSave = garageMapper.fromDto(garageDtoToSave);
        garageRepository.save(garageToSave);
        mockMvc.perform(delete("/garage/v1/delete?id=" + garageToSave.getGarageId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * check that @notnull validation works
     *
     * @throws Exception
     */
    @Test
    void delete_garage_id_empty() throws Exception {
        GarageDto garageDtoToSave = listGarageDtoToSave.getFirst();
        Garage garageToSave = garageMapper.fromDto(garageDtoToSave);
        garageRepository.save(garageToSave);
        mockMvc.perform(delete("/garage/v1/delete?id=").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void delete_garage_id_not_exist() throws Exception {
        mockMvc.perform(delete("/garage/v1/delete?id=" + 855875).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void get_all_garages_success_sort_by_name_asc() throws Exception {
        listGarageDtoToSave.stream().map(garageMapper::fromDto).forEach(garageRepository::save);
        MvcResult mvcResult = mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=name&direction=ASC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        List<GarageDto> garagesList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GarageDto>>() {
        });
        assertTrue(isSortedASC(garagesList.stream().map(GarageDto::getName).toList()));

    }

    @Test
    @Transactional
    void get_all_garages_success_sort_by_email_desc() throws Exception {
        listGarageDtoToSave.stream().map(garageMapper::fromDto).forEach(garageRepository::save);

        MvcResult mvcResultEmail = mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=email&direction=DESC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        List<GarageDto> garagesList = objectMapper.readValue(mvcResultEmail.getResponse().getContentAsString(), new TypeReference<List<GarageDto>>() {
        });
        assertTrue(isSortedDESC(garagesList.stream().map(GarageDto::getEmail).toList()));
    }


    public static boolean isSortedASC(List<String> listOfLongs) {
        if (isEmpty(listOfLongs) || listOfLongs.size() == 1) {
            return true;
        }

        Iterator<String> iter = listOfLongs.iterator();
        String current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (previous.compareTo(current) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    public static boolean isSortedDESC(List<String> listOfLongs) {
        if (isEmpty(listOfLongs) || listOfLongs.size() == 1) {
            return true;
        }

        Iterator<String> iter = listOfLongs.iterator();
        String current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (current.compareTo(previous) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }
}