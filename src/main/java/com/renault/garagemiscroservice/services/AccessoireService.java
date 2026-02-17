package com.renault.garagemiscroservice.services;

import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;

import java.util.List;

public interface AccessoireService {
    void createAccessoire(AccessoireDTO accessoireDTO) throws EntityNotFoundException, MethodArgumentNotValidException;
    void updateAccessoire(AccessoireDTO accessoireDTO) throws MethodArgumentNotValidException, EntityNotFoundException;
    void deleteAccessoire(Long id) throws MethodArgumentNotValidException, EntityNotFoundException;
    List<AccessoireDTO> getAllAccessoiresByVehicule(Long id) throws MethodArgumentNotValidException;

}
