package com.renault.garagemiscroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.services.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.renault.garagemiscroservice.utils.MessageGlobale.*;
import java.util.List;

@RestController
@RequestMapping("/vehicule/v1")
public class VehiculeController {


    private final VehiculeService  vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping("/create")
    public ResponseEntity<VehiculeDto> createVehicule(@RequestBody @Valid VehiculeDto vehicule) throws ArgumentNotValidException, EntityNotFoundException, JsonProcessingException, MaxVehiculeExceedException {
        VehiculeDto vehiculeDto=  vehiculeService.createVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeDto);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateVehicule(@RequestBody @Valid VehiculeDto vehicule) throws EntityNotFoundException, ArgumentNotValidException {
        vehiculeService.updateVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.OK).body(UPDATE_VEHICULE_MESSAGE);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteVehicule(@RequestParam @Valid Integer id) throws EntityNotFoundException, ArgumentNotValidException {
        vehiculeService.deleteVehicule(id);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_VEHICULE_MESSAGE);
    }

    @GetMapping("/by_garage")
    public ResponseEntity<List<VehiculeDto>> getVehiculesByGarage(@RequestParam @Valid Integer idGarage) throws ArgumentNotValidException, EntityNotFoundException {
       List<VehiculeDto> searchVehiculeResults=vehiculeService.getAllVehiculesByGarage(idGarage);
        return ResponseEntity.status(HttpStatus.OK).body(searchVehiculeResults);
    }

    @GetMapping("/by_model")
    public ResponseEntity<List<VehiculeDto>> getVehiculesByModel(@RequestParam @Valid String model) throws ArgumentNotValidException {
        List<VehiculeDto> searchVehiculeResults=vehiculeService.getAllVehiculesByModele(model);
        return ResponseEntity.status(HttpStatus.OK).body(searchVehiculeResults);
    }

}
