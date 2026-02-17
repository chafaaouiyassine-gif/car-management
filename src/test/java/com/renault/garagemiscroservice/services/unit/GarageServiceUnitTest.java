package com.renault.garagemiscroservice.services.unit;


import com.renault.garagemiscroservice.repositories.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
public class GarageServiceUnitTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    GarageRepository garageRepository;


}
