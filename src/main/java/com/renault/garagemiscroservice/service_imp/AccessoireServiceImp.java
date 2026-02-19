package com.renault.garagemiscroservice.service_imp;

import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.entities.Accessoire;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.mappers.AccessoireMapper;
import com.renault.garagemiscroservice.repositories.AccessoireRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import com.renault.garagemiscroservice.services.AccessoireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AccessoireServiceImp implements AccessoireService {
    private final AccessoireMapper accessoireMapper;
    private final VehiculeRepository vehiculeRepository;
    private final AccessoireRepository accessoireRepository;

    public AccessoireServiceImp(AccessoireMapper accessoireMapper, VehiculeRepository vehiculeRepository, AccessoireRepository accessoireRepository) {
        this.accessoireMapper = accessoireMapper;
        this.vehiculeRepository = vehiculeRepository;
        this.accessoireRepository = accessoireRepository;
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void createAccessoire(AccessoireDTO accessoireDTO) throws EntityNotFoundException, ArgumentNotValidException {
        log.info("Accessoire to save : {} ",accessoireDTO);
        if(Objects.isNull(accessoireDTO.getVehicule())) throw new ArgumentNotValidException();
        Vehicule vehicule=getVehiculeIfExists(accessoireDTO.getVehicule().getId());
        Accessoire accessoireToSave=accessoireMapper.fromDto(accessoireDTO);
        accessoireToSave.setVehicule(vehicule);
        accessoireRepository.save(accessoireToSave);
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void updateAccessoire(AccessoireDTO accessoireDTO) throws ArgumentNotValidException, EntityNotFoundException {
        if(getAccessoireIfExists(accessoireDTO.getId()).isPresent()){
            Accessoire accessoireToSave=accessoireMapper.fromDto(accessoireDTO);
            Vehicule vehicule=getVehiculeIfExists(accessoireToSave.getVehicule().getVehiculeId());
            accessoireToSave.setVehicule(vehicule);
            log.info("Accessoire to update : {} ",accessoireToSave);
            accessoireRepository.save(accessoireToSave);
        }else{
            log.info("Accessoire To update not found : {} ",accessoireDTO);
            throw new EntityNotFoundException();
        }
    }
    private Optional<Accessoire> getAccessoireIfExists(Integer id) throws ArgumentNotValidException {
        log.info("Id of accessoire to update : {} ",id);
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        return accessoireRepository.findById(id);
    }
    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void deleteAccessoire(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        Optional<Accessoire> accessoireToDelete=getAccessoireIfExists(id);
        if(accessoireToDelete.isPresent()){
            accessoireRepository.delete(accessoireToDelete.get());
        }else{
            throw new EntityNotFoundException();
        }

    }
    @Override
    public List<AccessoireDTO> getAllAccessoiresByVehicule(Integer id) throws ArgumentNotValidException {
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        return accessoireRepository.findAccessoireByVehiculeId(id).stream().map(accessoireMapper::toDto).toList();
    }

    private Vehicule getVehiculeIfExists(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        Optional<Vehicule> vehicule=vehiculeRepository.findById(id);
        if(vehicule.isEmpty()) throw new EntityNotFoundException();
        return vehicule.get();
    }
}
