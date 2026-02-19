package com.renault.garagemiscroservice.service_imp;

import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import com.renault.garagemiscroservice.exceptions.EntityNotFoundException;
import com.renault.garagemiscroservice.exceptions.ArgumentNotValidException;
import com.renault.garagemiscroservice.mappers.GarageMapper;
import com.renault.garagemiscroservice.repositories.GarageRepository;
import com.renault.garagemiscroservice.services.GarageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GarageServiceImp implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    public GarageServiceImp(GarageRepository garageRepository, GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
    }

    /**
     * this method will create a garage with horaire ouverture
     *
     * @param garage the garage to save
     */
    @Override
    public GarageDto saveGarage(GarageDto garage) throws ArgumentNotValidException {
        log.info("garage to save : {} ", garage);
        chechIfGarageIsNull(garage);
        chechIfIdGarageNotNull(garage);
        return createGarage(garage);
    }

    public GarageDto createGarage(GarageDto garage) {
        Garage garageToSave = garageMapper.fromDto(garage);
        garageRepository.save(garageToSave);
        return garageMapper.toDto(garageToSave);
    }


    private void chechIfGarageIsNull(GarageDto garage) throws ArgumentNotValidException {
        if (Objects.isNull(garage))
            throw new ArgumentNotValidException();
    }

    private void chechIfIdGarageNotNull(GarageDto garage) throws ArgumentNotValidException {
        if (!Objects.isNull(garage.getId()))
            throw new ArgumentNotValidException();
    }

    /**
     * this methode will update the garage entity if it exists in db
     *
     * @param garage the entity to update
     */
    @Override
    public void updateGarage(GarageDto garage) throws ArgumentNotValidException, EntityNotFoundException {
        log.info("garage to update : {} ", garage);
        chechIfGarageIsNull(garage);
        if (!Objects.isNull(garage.getId()) && findGarageByID(garage.getId()).isPresent()) {
            createGarage(garage);
        } else {
            throw new EntityNotFoundException();
        }
    }


    /**
     * try to find a garage by geving id
     *
     * @param id the id to look for
     * @return the Garage entity
     */

    @Override
    public GarageDto getGarageById(Integer id) throws EntityNotFoundException {
        log.info("find garage by id : {} ", id);
        Optional<Garage> garage = findGarageByID(id);
        if (garage.isPresent()) {
            return garageMapper.toDto(garage.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * check if the id is not null and delete Garage if exists
     *
     * @param id Garage id to delete
     * @throws ArgumentNotValidException if the id is null
     */
    @Override
    public void deleteGarage(Integer id) throws ArgumentNotValidException, EntityNotFoundException {
        if (Objects.isNull(id)) throw new ArgumentNotValidException();
        Optional<Garage> garage = findGarageByID(id);
        if (garage.isEmpty()) throw new EntityNotFoundException();
        garageRepository.delete(garage.get());
    }

    /**
     * get List of garages sorted by custom value
     *
     * @param pageable to create pagination
     * @return List<GarageDto>
     */
    @Override
    public List<GarageDto> getAllGarageSorted(Pageable pageable) {
        return garageRepository.findAll(pageable).stream()
                .map(garageMapper::toDto)
                .toList();
    }

    @Override
    public List<GarageDto> getAllGarageByTypeVehiculeORAccessoire(TypeVehicule type, Integer accessoireId) throws ArgumentNotValidException {
        if (Objects.isNull(type)) throw new ArgumentNotValidException();
        if (Objects.isNull(accessoireId)) throw new ArgumentNotValidException();
        return garageRepository.findGarageByTypeVehiculeAndAccessoire(type, accessoireId)
                .orElse(new ArrayList<>()).stream().map(garageMapper::toDto).toList();
    }


    /**
     * check if the id of garage is not null and that the garage exists in DB
     *
     * @param id entity id to check
     * @return boolean
     */
    private Optional<Garage> findGarageByID(Integer id) {
        if (Objects.isNull(id)) return Optional.empty();
        return garageRepository
                .findById(id);
    }


}
