package com.renault.garagemiscroservice.controller;

import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;
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
    public ResponseEntity<String> createVehicule(@RequestBody @Valid VehiculeDto vehicule) throws MethodArgumentNotValidException, EntityNotFoundException {
        vehiculeService.createVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(CREATE_VEHICULE_MESSAGE);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateVehicule(@RequestBody @Valid VehiculeDto vehicule) throws EntityNotFoundException, MethodArgumentNotValidException {
        vehiculeService.updateVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.OK).body(UPDATE_VEHICULE_MESSAGE);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteVehicule(@RequestParam @Valid Long id) throws EntityNotFoundException, MethodArgumentNotValidException {
        vehiculeService.deleteVehicule(id);
        return ResponseEntity.status(HttpStatus.OK).body(UPDATE_VEHICULE_MESSAGE);
    }

    @GetMapping("/by_garage")
    public ResponseEntity<List<VehiculeDto>> getVehiculesByGarage(@RequestParam @Valid Long idGarage) throws MethodArgumentNotValidException {
       List<VehiculeDto> searchVehiculeResults=vehiculeService.getAllVehiculesByGarage(idGarage);
        return ResponseEntity.status(HttpStatus.OK).body(searchVehiculeResults);
    }

    @GetMapping("/by_model")
    public ResponseEntity<List<VehiculeDto>> getVehiculesByModel(@RequestParam @Valid String model) throws MethodArgumentNotValidException {
        List<VehiculeDto> searchVehiculeResults=vehiculeService.getAllVehiculesByModele(model);
        return ResponseEntity.status(HttpStatus.OK).body(searchVehiculeResults);
    }

}
