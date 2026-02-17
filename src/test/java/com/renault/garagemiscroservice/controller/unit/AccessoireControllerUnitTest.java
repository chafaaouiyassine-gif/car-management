package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.*;
import com.renault.garagemiscroservice.enums.DayOfWeek;
import com.renault.garagemiscroservice.enums.TypeCarburant;
import com.renault.garagemiscroservice.services.AccessoireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class AccessoireControllerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccessoireService accessoireService;

    @Autowired
    ObjectMapper objectMapper;

    AccessoireDTO accessoireDTO;

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
        GarageDto garageDto=GarageDto.builder()
                .telephone("0584857596")
                .email("test@gmail.com")
                .address(adresseDto)
                .name("garage 1")
                .horaireOvertureList(List.of(horaireOvertureDto))
                .build();

       VehiculeDto vehiculeDto= VehiculeDto.builder()
                .model("2015")
                .brand("Mercedess")
                .anneeFabrication("2012")
                .typeCarburant(TypeCarburant.DIESEL)
                .garage(garageDto)
                .build();

       accessoireDTO=AccessoireDTO.builder()
               .nom("accessoire 1")
               .description("description 1")
               .prix(152.52f)
               .type("moteur")
               .vehicule(vehiculeDto)
               .build();
    }
    @Test
    void create_accessoire_success() throws Exception {
        String accessoirDtoJson=objectMapper.writeValueAsString(accessoireDTO);
        doNothing().when(accessoireService).createAccessoire(any(AccessoireDTO.class));
        mockMvc.perform(post("/accessoire/v1/create").contentType(MediaType.APPLICATION_JSON).content(accessoirDtoJson)).andExpect(status().isCreated());
        verify(accessoireService,times(1)).createAccessoire(any(AccessoireDTO.class));

    }
    @Test
    void update_accessoir_success() throws Exception {
        String accessoirDtoJson=objectMapper.writeValueAsString(accessoireDTO);
        doNothing().when(accessoireService).updateAccessoire(any(AccessoireDTO.class));
        mockMvc.perform(put("/accessoire/v1/update").contentType(MediaType.APPLICATION_JSON).content(accessoirDtoJson)).andExpect(status().isOk());
        verify(accessoireService,times(1)).updateAccessoire(any(AccessoireDTO.class));

    }
    @Test
    void delete_accessoir_success() throws Exception {
        String accessoireDtoJson=objectMapper.writeValueAsString(accessoireDTO);
        doNothing().when(accessoireService).deleteAccessoire(any(Integer.class));
        mockMvc.perform(delete("/accessoire/v1/delete?id=1").contentType(MediaType.APPLICATION_JSON).content(accessoireDtoJson)).andExpect(status().isOk());
        verify(accessoireService,times(1)).deleteAccessoire(any(Integer.class));
    }
    @Test
    void find_by_vehicule_success() throws Exception {
        when(accessoireService.getAllAccessoiresByVehicule(1)).thenReturn(List.of(accessoireDTO));
        mockMvc.perform(get("/accessoire/v1/by_vehicule?idVehicule=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(accessoireService,times(1)).getAllAccessoiresByVehicule(any(Integer.class));

    }
}