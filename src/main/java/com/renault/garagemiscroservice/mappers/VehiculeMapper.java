package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.VehiculeDto;
import com.renault.garagemiscroservice.entities.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(source = "vehiculeId",target = "id")
    VehiculeDto toDto(Vehicule vehicule);
    @Mapping(target = "vehiculeId",source = "id")
    @Mapping(target = "garage.garageId",source = "garage.id")
    Vehicule fromDto(VehiculeDto vehiculeDto);
}
