package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.entities.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  GarageMapper {
    @Mapping(source = "garageId",target = "id")
    GarageDto toDto(Garage garage);
    @Mapping(target = "garageId",source = "id")
    Garage fromDto(GarageDto garageDto);
}
