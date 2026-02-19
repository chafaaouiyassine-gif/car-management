package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.controller.VehiculeController;
import com.renault.garagemiscroservice.dto.*;
import com.renault.garagemiscroservice.services.VehiculeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.renault.garagemiscroservice.utils.DtoAndEntitiesGenerator.generateVehiculeDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehiculeController.class)
class VehiculeControllerUnitTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private VehiculeService vehiculeService;
    @Autowired
    ObjectMapper objectMapper;

   VehiculeDto vehiculeDto;
    @BeforeEach
    void setUp()  {
        vehiculeDto=generateVehiculeDto();
    }
    @Test
    void create_garage_success() throws Exception {
        String vehiculeDtoJson=objectMapper.writeValueAsString(vehiculeDto);
        when(vehiculeService.createVehicule(any(VehiculeDto.class))).thenReturn(vehiculeDto);
        mockMvc.perform(post("/vehicule/v1/create").contentType(MediaType.APPLICATION_JSON).content(vehiculeDtoJson)).andExpect(status().isCreated());
        verify(vehiculeService,times(1)).createVehicule(any(VehiculeDto.class));

    }
    @Test
    void update_garage_success() throws Exception {
        String vehiculeDtoJson=objectMapper.writeValueAsString(vehiculeDto);
        doNothing().when(vehiculeService).updateVehicule(any(VehiculeDto.class));
        mockMvc.perform(put("/vehicule/v1/update").contentType(MediaType.APPLICATION_JSON).content(vehiculeDtoJson)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).updateVehicule(any(VehiculeDto.class));

    }
    @Test
    void delete_vehicule_success() throws Exception {
        String vehiculeDtoJson=objectMapper.writeValueAsString(vehiculeDto);
        doNothing().when(vehiculeService).deleteVehicule(any(Integer.class));
        mockMvc.perform(delete("/vehicule/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON).content(vehiculeDtoJson)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).deleteVehicule(any(Integer.class));
    }
    @Test
    void find_by_garage_success() throws Exception {
        when(vehiculeService.getAllVehiculesByGarage(1)).thenReturn(List.of(vehiculeDto));
        mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).getAllVehiculesByGarage(any(Integer.class));

    }
    @Test
    void find_by_model_success() throws Exception {
        when(vehiculeService.getAllVehiculesByModele("2015")).thenReturn(List.of(vehiculeDto));
        mockMvc.perform(get("/vehicule/v1/by_model?model=2015").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).getAllVehiculesByModele(any(String.class));

    }
}