package com.renault.garagemiscroservice.services.integ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.services.GarageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootTest
@Transactional
public class GarageServiceIntegTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageService garageService;

    @Autowired
    GarageMapper garageMapper;

    @Value("classpath:garage/list_garage_to_save.json")
    private Resource garageResource;

    List<GarageDto> listGarageDtoToSave ;

    @BeforeEach
    void setUp() throws IOException {
        listGarageDtoToSave = objectMapper.readValue(garageResource.getInputStream(), new TypeReference<List<GarageDto>>() {
        });
    }


    @Test
    void create_garage_success() throws Exception {
        GarageDto  savedGarage=garageService.saveGarage(listGarageDtoToSave.getFirst());
        assertNotNull(savedGarage.getId());
        Optional<Garage> garage= garageRepository.findById(savedGarage.getId());
        assert garage.isPresent();
        assertEquals("OISIS Garage",garage.get().getName());
    }
    @Test
    void create_garage_id_not_null() {
        GarageDto garageDtoToSave=listGarageDtoToSave.getFirst();
        garageDtoToSave.setId(1);
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            garageService.saveGarage(garageDtoToSave);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void create_garage__null()  {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            garageService.saveGarage(null);
        });
        assertEquals("Argument not valid", exception.getMessage());
    }
    @Test
    void update_garage_success() throws Exception {
        GarageDto garageToUpdate=listGarageDtoToSave.getFirst();
        Garage garage=garageRepository.save(garageMapper.fromDto(garageToUpdate));
        assertNotNull(garage.getGarageId());
        garageToUpdate.setId(garage.getGarageId());
        garageToUpdate.setName("Test 1");
        garageService.updateGarage(garageToUpdate);
        Optional<Garage> garageUpdated= garageRepository.findById(garage.getGarageId());
        assertTrue(garageUpdated.isPresent());
        assertEquals("Test 1",garageUpdated.get().getName());
    }
    @Test
    void update_garage_entity_not_found()  {
        GarageDto garageToUpdate=listGarageDtoToSave.getFirst();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            garageService.updateGarage(garageToUpdate);
        });
        assertEquals("Entity not found", exception.getMessage());

    }
    @Test
    void update_garage_entity_null()  {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            garageService.updateGarage(null);
        });
        assertEquals("Argument not valid", exception.getMessage());

    }
    @Test
    void delete_garage_success() throws Exception {
        Garage garage=garageRepository.save(garageMapper.fromDto(listGarageDtoToSave.getFirst()));
        assertNotNull(garage.getGarageId());
        garageService.deleteGarage(garage.getGarageId());
        Optional<Garage> garageUpdated= garageRepository.findById(garage.getGarageId());
        assertTrue(garageUpdated.isEmpty());
    }
    @Test
    void delete_garage_id_null()  {
        ArgumentNotValidException exception = assertThrows(ArgumentNotValidException.class, () -> {
            garageService.deleteGarage(null);
        });
        assertEquals("Argument not valid", exception.getMessage());

    }
    @Test
    void delete_garage_not_exists()  {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            garageService.deleteGarage(75142);
        });
        assertEquals("Entity not found", exception.getMessage());

    }
    @Test
    void get_by_id_garage_success() throws Exception {
        Garage garage= garageRepository.save(garageMapper.fromDto(listGarageDtoToSave.getFirst()));
        GarageDto chhosenGarage=garageService.getGarageById(garage.getGarageId());
        assertNotEquals(null,chhosenGarage);
    }
    @Test
    void get_by_id_garage_id_null()  {

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            garageService.getGarageById(null);
        });
        assertEquals("Entity not found", exception.getMessage());

    }
    @Test
    void get_all_garage_success()  {
        garageRepository.deleteAll();
        listGarageDtoToSave.stream().map(garageMapper::fromDto).forEach(garage->{
            garageRepository.save(garage);
        });
        Sort sort= Sort.by(Sort.Direction.fromString("ASC"),"garageId");
        Pageable pageable
                = PageRequest.of(0,3,sort);
        List<GarageDto> garageList=garageService.getAllGarageSorted(pageable);
        assertEquals(3, garageList.size());
        assert isSorted(garageList.stream().map(GarageDto::getId).toList());
    }
    public static boolean isSorted(List<Integer> listOfLongs) {
        if (isEmpty(listOfLongs) || listOfLongs.size() == 1) {
            return true;
        }

        Iterator<Integer> iter = listOfLongs.iterator();
        Integer current, previous = iter.next();
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
