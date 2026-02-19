package com.renault.garagemiscroservice.services;

import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;

import java.util.List;

public interface AccessoireService {
    void createAccessoire(AccessoireDTO accessoireDTO) throws EntityNotFoundException, ArgumentNotValidException;
    void updateAccessoire(AccessoireDTO accessoireDTO) throws ArgumentNotValidException, EntityNotFoundException;
    void deleteAccessoire(Integer id) throws ArgumentNotValidException, EntityNotFoundException;
    List<AccessoireDTO> getAllAccessoiresByVehicule(Integer id) throws ArgumentNotValidException;

}
