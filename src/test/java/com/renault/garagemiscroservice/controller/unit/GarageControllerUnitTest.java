package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.AdresseDto;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.HoraireOvertureDto;
import com.renault.garagemiscroservice.dto.OpeningTimeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.enums.DayOfWeek;
import com.renault.garagemiscroservice.services.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GarageControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private GarageService garageService;

    @Autowired
    ObjectMapper objectMapper;

    private GarageDto garageDto;
    @BeforeEach
    void setUp(){
        AdresseDto adresseDto=AdresseDto.builder()
                .rue("teswt")
                .numero(15)
                .ville("casablanca")
                .pays("Maroc")
                .build();
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder().
                endTime(LocalTime.MAX)
                .startyTime(LocalTime.MIN)
                .build();

        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .dayOfWeek(DayOfWeek.LUNDI)
                .openingTimeList(List.of(openingTimeDto))
                .build();
        garageDto=GarageDto.builder()
                .telephone("0584857596")
                .email("test@gmail.com")
                .address(adresseDto)
                .name("garage 1")
                .horaireOvertureList(List.of(horaireOvertureDto))
                .build();
    }
    @Test
    void create_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(garageDto);
        when(garageService.saveGarage(any(GarageDto.class))).thenReturn(null);
        mockMvc.perform(post("/garage/v1/create").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isCreated());
        verify(garageService,times(1)).saveGarage(any(GarageDto.class));

    }
    @Test
    void update_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(garageDto);
        doNothing().when(garageService).updateGarage(any(GarageDto.class));
        mockMvc.perform(put("/garage/v1/update").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).updateGarage(any(GarageDto.class));

    }
    @Test
    void delete_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(garageDto);
        doNothing().when(garageService).deleteGarage(any(Integer.class));
        mockMvc.perform(delete("/garage/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).deleteGarage(any(Integer.class));
    }
    @Test
    void find_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(garageDto);
        when(garageService.getGarageById(1)).thenReturn(garageDto);
        mockMvc.perform(get("/garage/v1/find?id=1").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).getGarageById(any(Integer.class));

    }
    @Test
    void all_garage_success() throws Exception {
        String garageDtoJson=objectMapper.writeValueAsString(garageDto);
        Sort sort= Sort.by(Sort.Direction.fromString("DESC"),"name");
        Pageable pageable
                = PageRequest.of(0,3,sort);
        when(garageService.getAllGarageSorted(pageable)).thenReturn(List.of(garageDto));
        mockMvc.perform(get("/garage/v1/all?page=0&size=3&sortValue=name&direction=DESC").contentType(MediaType.APPLICATION_JSON).content(garageDtoJson)).andExpect(status().isOk());
        verify(garageService,times(1)).getAllGarageSorted(any(Pageable.class));

    }
}