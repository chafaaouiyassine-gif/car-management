package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.controller.GarageController;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.services.GarageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.renault.garagemiscroservice.utils.DtoAndEntitiesGenerator.generateGarageDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GarageController.class) // Charge uniquement la couche Web
class GarageControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GarageService garageService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(generateGarageDto());
        when(garageService.saveGarage(any(GarageDto.class))).thenReturn(null);
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isCreated());
        verify(garageService,times(1)).saveGarage(any(GarageDto.class));

    }

    @Test
    void update_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(generateGarageDto());
        doNothing().when(garageService).updateGarage(any(GarageDto.class));
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).updateGarage(any(GarageDto.class));

    }

    @Test
    void delete_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(generateGarageDto());
        doNothing().when(garageService).deleteGarage(any(Integer.class));
        mockMvc.perform(delete("/garage/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).deleteGarage(any(Integer.class));
    }
    @Test
    void find_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(generateGarageDto());
        when(garageService.getGarageById(1)).thenReturn(generateGarageDto());
        mockMvc.perform(get("/garage/v1/find?id=1").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).getGarageById(any(Integer.class));

    }

    @Test
    void all_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(generateGarageDto());
        Sort sort= Sort.by(Sort.Direction.fromString("DESC"),"name");
        Pageable pageable
                = PageRequest.of(0,3,sort);
        when(garageService.getAllGarageSorted(pageable)).thenReturn(List.of(generateGarageDto()));
        mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=name&direction=DESC").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).getAllGarageSorted(any(Pageable.class));

    }
}