package com.renault.garagemiscroservice.service_imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.entities.Vehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.MaxVehiculeExceedException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.mappers.VehiculeMapper;
import com.renault.garagemiscroservice.producers.VehiculeProducer;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.repositories.VehiculeRepository;
import com.renault.garagemiscroservice.services.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Value("${vehicule.max}")
    int maxVehicul;

    public VehiculeServiceImp(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper, GarageRepository garageRepository, VehiculeProducer vehiculeProducer) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
        this.garageRepository = garageRepository;
        this.vehiculeProducer = vehiculeProducer;
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public VehiculeDto createVehicule(VehiculeDto vehiculeDto) throws ArgumentNotValidException, EntityNotFoundException, JsonProcessingException, MaxVehiculeExceedException {
        log.info("Vehicule to save : {} ",vehiculeDto);
        if(Objects.isNull(vehiculeDto.getGarage())) throw new ArgumentNotValidException();
        Garage garage=findGarageIfExists(vehiculeDto.getGarage().getId());
        if(garage.getCountVehicule()<maxVehicul){
            garage.setCountVehicule(garage.getCountVehicule()+1);
            garageRepository.save(garage);
            Vehicule vehiculeToSave=vehiculeMapper.fromDto(vehiculeDto);
            vehiculeToSave.setGarage(garage);
            vehiculeRepository.save(vehiculeToSave);
            vehiculeProducer.sendVehicule(vehiculeToSave);
            return vehiculeMapper.toDto(vehiculeToSave);
        }else
        {
            throw new MaxVehiculeExceedException("Max vehicules");
        }
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void updateVehicule(VehiculeDto vehiculeDto) throws ArgumentNotValidException, EntityNotFoundException {
        if(Objects.isNull(vehiculeDto)) throw new ArgumentNotValidException();
        if(getVehiculeIfExists(vehiculeDto.getId()).isPresent()){
            Vehicule vehiculeToSave=vehiculeMapper.fromDto(vehiculeDto);
            vehiculeToSave.setGarage(findGarageIfExists(vehiculeToSave.getGarage().getGarageId()));
            log.info("Vehicule to update : {} ",vehiculeToSave);
            vehiculeRepository.save(vehiculeToSave);
        }else{
            log.info("Vehicule To update not found : {} ",vehiculeDto);
            throw new EntityNotFoundException();
        }
    }

    @Override
    @Transactional(rollbackFor ={Exception.class})
    public void deleteVehicule(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        Optional<Vehicule> vahiculeToDelete=getVehiculeIfExists(id);
        if(vahiculeToDelete.isPresent()){
            Garage garage=vahiculeToDelete.get().getGarage();
            vehiculeRepository.delete(vahiculeToDelete.get());
            garage.setCountVehicule(garage.getCountVehicule()-1);
            garageRepository.save(garage);
        }else{
            throw new EntityNotFoundException();
        }

    }

    @Override
    @Transactional
    public List<VehiculeDto> getAllVehiculesByGarage(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        if(garageRepository.findById(id).isEmpty()) throw new EntityNotFoundException();
        return vehiculeRepository.findVehiculeByGarageId(id)
                .map(vehiculeList -> vehiculeList.stream().map(vehiculeMapper::toDto).toList())
                .orElseGet(ArrayList::new);
    }

    @Override
    @Transactional
    public List<VehiculeDto> getAllVehiculesByModele(String modele) throws ArgumentNotValidException {
        if(Objects.isNull(modele)) throw new ArgumentNotValidException();
        return vehiculeRepository.findByModel(modele).stream().map(vehiculeMapper::toDto).toList();
    }

    private Optional<Vehicule> getVehiculeIfExists(Integer id) throws ArgumentNotValidException {
        log.info("Id of vihecule to update : {} ",id);
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        return vehiculeRepository.findById(id);
    }

    public Garage findGarageIfExists(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        if(Objects.isNull(id)) throw new ArgumentNotValidException();
        Optional<Garage> garage=garageRepository.findById(id);
        if(garage.isEmpty()) throw new EntityNotFoundException();

        return garage.get();
    }

    public void setMaxVehicul(int maxVehicul) {
        this.maxVehicul = maxVehicul;
    }
}
