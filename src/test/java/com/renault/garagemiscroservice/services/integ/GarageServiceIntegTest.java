package com.renault.garagemiscroservice.services.integ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.AdresseDto;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.HoraireOvertureDto;
import com.renault.garagemiscroservice.dto.OpeningTimeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.enums.DayOfWeek;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.services.GarageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GarageServiceIntegTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageService garageService;

    @Autowired
    GarageMapper garageMapper;

    GarageDto garageDto;
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
        garageDto= GarageDto.builder()
                .id(null)
                .telephone("0584857596")
                .email("test@gmail.com")
                .address(adresseDto)
                .name("garage 1")
                .horaireOvertureList(List.of(horaireOvertureDto))
                .build();
    }


    @Test
@Order(1)
    void create_garage_success() throws Exception {
        garageService.saveGarage(garageDto);
        Optional<Garage> garage= garageRepository.findById(1L);
        assert garage.isPresent();
        assertEquals(garageDto.getName(),garage.get().getName());
    }
    @Test
    @Order(2)
    void update_garage_success() throws Exception {
        garageService.saveGarage(garageDto);
        garageDto.setName("test");
        garageDto.setId(2L);
        garageService.updateGarage(garageDto);
        Optional<Garage> garageUpdated= garageRepository.findById(2L);
        assert garageUpdated.isPresent();
        assertEquals("test",garageUpdated.get().getName());
    }
    @Test
    @Order(3)
    void delete_garage_success() throws Exception {
        garageService.saveGarage(garageDto);
        garageService.deleteGarage(3L);
        Optional<Garage> garageUpdated= garageRepository.findById(3L);
        assertTrue(garageUpdated.isEmpty());
    }
    @Test
    @Order(4)
    void get_by_id_garage_success() throws Exception {
        garageRepository.save(garageMapper.fromDto(garageDto));
        GarageDto garage=garageService.getGarageById(4L);
       assertNotEquals(null,garage);
    }
    @Test
    @Order(5)
    void get_all_garage_success()  {
        for(int i=0;i<3;i++){
            garageRepository.save(garageMapper.fromDto(garageDto));
        }
        Sort sort= Sort.by(Sort.Direction.fromString("ASC"),"garageId");
        Pageable pageable
                = PageRequest.of(0,3,sort);
        List<GarageDto> garageList=garageService.getAllGarageSorted(pageable);
        assertEquals(3, garageList.size());
        assert isSorted(garageList.stream().map(GarageDto::getId).toList());
    }
    public static boolean isSorted(List<Long> listOfLongs) {
        if (isEmpty(listOfLongs) || listOfLongs.size() == 1) {
            return true;
        }

        Iterator<Long> iter = listOfLongs.iterator();
        Long current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (previous.compareTo(current) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }
}
