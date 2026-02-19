package com.renault.garagemiscroservice.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.controller.AccessoireController;
import com.renault.garagemiscroservice.dto.*;
import com.renault.garagemiscroservice.services.AccessoireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.renault.garagemiscroservice.utils.DtoAndEntitiesGenerator.generateAccessoireDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccessoireController.class)
class AccessoireControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccessoireService accessoireService;

    AccessoireDTO accessoireDTO;

    @BeforeEach
    void setUp()  {

        accessoireDTO=generateAccessoireDto();

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