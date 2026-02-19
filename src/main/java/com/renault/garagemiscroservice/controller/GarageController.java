package com.renault.garagemiscroservice.controller;

import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.services.GarageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.renault.garagemiscroservice.utils.MessageGlobale.*;
import java.util.List;

@RestController
@RequestMapping("/garage/v1")
public class GarageController {

    private final GarageService garageService;


    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping("/create")
    public ResponseEntity<GarageDto> createGarage(@RequestBody @Valid GarageDto garage) throws ArgumentNotValidException {
        GarageDto garageDto=garageService.saveGarage(garage);
        return ResponseEntity.status(HttpStatus.CREATED).body(garageDto);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateGarage(@RequestBody @Valid GarageDto garage) throws ArgumentNotValidException, EntityNotFoundException {
        garageService.updateGarage(garage);
        return ResponseEntity.status(HttpStatus.OK).body(UPDATE_SUCCESS_MESSAGE);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGarage(@RequestParam @NotNull Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        garageService.deleteGarage(id);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESS_MESSAGE);
    }
    @GetMapping("/find")
    public ResponseEntity<GarageDto> getGarage(@RequestParam @NotNull Integer id) throws EntityNotFoundException {
      GarageDto garageDto= garageService.getGarageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(garageDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GarageDto>> getAllGaragesSorted(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "0") int size
                                                               ,@RequestParam(defaultValue = "name") String sortValue,@RequestParam(defaultValue = "DESC") String direction){
        Sort sort= Sort.by(Sort.Direction.fromString(direction),sortValue);
        Pageable pageable
                = PageRequest.of(page,size,sort);
        List<GarageDto> garages=garageService.getAllGarageSorted(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(garages);
    }
    @GetMapping("/find_garages")
    public ResponseEntity<List<GarageDto>> getAllGarage(@RequestParam @NotNull TypeVehicule type,@RequestParam @NotNull Integer accessoireID) throws  ArgumentNotValidException {
        List<GarageDto> garages= garageService.getAllGarageByTypeVehiculeORAccessoire(type,accessoireID);
        return ResponseEntity.status(HttpStatus.OK).body(garages);
    }
}
