package com.renault.garagemiscroservice.service_imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MethodArgumentNotValidException;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.producers.VehiculeProducer;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import com.renault.garagemiscroservice.services.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class VehiculeServiceImp implements VehiculeService {

    VehiculeRepository vehiculeRepository;
    VehiculeMapper vehiculeMapper;
    GarageRepository garageRepository;
    VehiculeProducer vehiculeProducer;

    public VehiculeServiceImp(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper, GarageRepository garageRepository, VehiculeProducer vehiculeProducer) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
        this.garageRepository = garageRepository;
        this.vehiculeProducer = vehiculeProducer;
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void createVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException, JsonProcessingException {
        log.info("Vehicule to save : {} ",vehiculeDto);
        Garage garage=findGarageIfExists(vehiculeDto.getGarage().getId());
        Vehicule vehiculeToSave=vehiculeMapper.fromDto(vehiculeDto);
        vehiculeToSave.setGarage(garage);
        vehiculeRepository.save(vehiculeToSave);
        vehiculeProducer.sendVehicule(vehiculeToSave);

    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void updateVehicule(VehiculeDto vehiculeDto) throws MethodArgumentNotValidException, EntityNotFoundException {
        if(getVehiculeIfExists(vehiculeDto.getId()).isPresent()){
            Vehicule vehiculeToSave=vehiculeMapper.fromDto(vehiculeDto);
            vehiculeToSave.setGarage(findGarageIfExists(vehiculeToSave.getGarage().getGarageId()));
            log.info("Vehicule to update : {} ",vehiculeToSave);
            vehiculeRepository.save(vehiculeToSave);
        }else{
            log.info("Vehicule To update not found : {} ",vehiculeDto);
            throw new EntityNotFoundException("");
        }
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void deleteVehicule(Integer id) throws MethodArgumentNotValidException, EntityNotFoundException {
        Optional<Vehicule> vahiculeToDelete=getVehiculeIfExists(id);
        if(vahiculeToDelete.isPresent()){
            vehiculeRepository.delete(vahiculeToDelete.get());
        }else{
            throw new EntityNotFoundException("");
        }

    }

    @Override
    public List<VehiculeDto> getAllVehiculesByGarage(Integer id) throws MethodArgumentNotValidException {
        if(Objects.isNull(id)) throw new MethodArgumentNotValidException("");
        return vehiculeRepository.findVehiculeByGarageId(id).stream().map(vehiculeMapper::toDto).toList();
    }

    @Override
    public List<VehiculeDto> getAllVehiculesByModele(String modele) throws MethodArgumentNotValidException {
        if(Objects.isNull(modele)) throw new MethodArgumentNotValidException("");
        return vehiculeRepository.findByModel(modele).stream().map(vehiculeMapper::toDto).toList();
    }

    private Optional<Vehicule> getVehiculeIfExists(Integer id) throws MethodArgumentNotValidException {
        log.info("Id of vihecule to update : {} ",id);
        if(Objects.isNull(id)) throw new MethodArgumentNotValidException("");
        return vehiculeRepository.findById(id);
    }

    private Garage findGarageIfExists(Integer id) throws MethodArgumentNotValidException, EntityNotFoundException {
        if(Objects.isNull(id)) throw new MethodArgumentNotValidException("");
        Optional<Garage> garage=garageRepository.findById(id);
        if(garage.isEmpty()) throw new EntityNotFoundException("");
        return garage.get();
    }
}
