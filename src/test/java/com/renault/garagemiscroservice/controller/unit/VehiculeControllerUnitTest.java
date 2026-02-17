package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.*;
import com.renault.garagemiscroservice.enums.DayOfWeek;
import com.renault.garagemiscroservice.enums.TypeCarburant;
import com.renault.garagemiscroservice.services.VehiculeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculeControllerUnitTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private VehiculeService vehiculeService;
    @Autowired
    ObjectMapper objectMapper;

    private VehiculeDto vehiculeDto;

    @BeforeEach
    void setUp(){
        AdresseDto adresseDto=AdresseDto.builder()
                .id(1L)
                .rue("teswt")
                .numero(15)
                .ville("casablanca")
                .pays("Maroc")
                .build();
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder().
                endTime(LocalDate.now())
                .id(1L)
                .startyTime(LocalDate.now())
                .build();

        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.LUNDI)
                .openingTimeList(List.of(openingTimeDto))
                .build();
       GarageDto garageDto=GarageDto.builder()
                .id(1L)
                .telephone("0584857596")
                .email("test@gmail.com")
                .address(adresseDto)
                .name("garage 1")
                .horaireOvertureList(List.of(horaireOvertureDto))
                .build();

        vehiculeDto=VehiculeDto.builder()
                .model("2015")
                .brand("Mercedess")
                .anneeFabrication("2012")
                .typeCarburant(TypeCarburant.DIESEL)
                .id(1L)
                .garage(garageDto)
                .build();
    }
    @Test
    void create_garage_success() throws Exception {
        String vehiculeDtoJson=objectMapper.writeValueAsString(vehiculeDto);
        doNothing().when(vehiculeService).createVehicule(any(VehiculeDto.class));
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
        doNothing().when(vehiculeService).deleteVehicule(any(Long.class));
        mockMvc.perform(delete("/vehicule/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON).content(vehiculeDtoJson)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).deleteVehicule(any(Long.class));
    }
    @Test
    void find_by_garage_success() throws Exception {
        when(vehiculeService.getAllVehiculesByGarage(1L)).thenReturn(List.of(vehiculeDto));
        mockMvc.perform(get("/vehicule/v1/by_garage?idGarage=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).getAllVehiculesByGarage(any(Long.class));

    }
    @Test
    void find_by_model_success() throws Exception {
        when(vehiculeService.getAllVehiculesByModele("2015")).thenReturn(List.of(vehiculeDto));
        mockMvc.perform(get("/vehicule/v1/by_model?model=2015").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(vehiculeService,times(1)).getAllVehiculesByModele(any(String.class));

    }
}