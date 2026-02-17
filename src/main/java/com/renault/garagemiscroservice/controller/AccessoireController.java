package com.renault.garagemiscroservice.controller;

import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;
import com.renault.garagemiscroservice.services.AccessoireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.renault.garagemiscroservice.utils.MessageGlobale.*;

import java.util.List;

@RestController
@RequestMapping("/accessoire/v1")
public class AccessoireController {


    private final AccessoireService accessoireService;

    public AccessoireController(AccessoireService accessoireService) {
        this.accessoireService = accessoireService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccessoire(@RequestBody @Valid AccessoireDTO accessoireDTO) throws EntityNotFoundException, MethodArgumentNotValidException {
        accessoireService.createAccessoire(accessoireDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ACCESSOIRE_CREATE_MESSAGE);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateAccessoire(@RequestBody @Valid AccessoireDTO accessoireDTO) throws EntityNotFoundException, MethodArgumentNotValidException {
        accessoireService.updateAccessoire(accessoireDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ACCESSOIRE_UPDATE_MESSAGE);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccessoire(@RequestParam @Valid Integer id) throws EntityNotFoundException, MethodArgumentNotValidException {
        accessoireService.deleteAccessoire(id);
        return ResponseEntity.status(HttpStatus.OK).body(ACCESSOIRE_DELETE_MESSAGE);
    }
    @GetMapping("/by_vehicule")
    public ResponseEntity<List<AccessoireDTO>> getAccessoireByVehicule(@RequestParam @Valid Integer idVehicule) throws MethodArgumentNotValidException {
        List<AccessoireDTO> searcheAccessoireResult=accessoireService.getAllAccessoiresByVehicule(idVehicule);
        return ResponseEntity.status(HttpStatus.OK).body(searcheAccessoireResult);
    }

}
