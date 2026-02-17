package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.AccessoireDTO;
import com.renault.garagemiscroservice.entities.Accessoire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessoireMapper {
    @Mapping(source = "accessoireId",target = "id")
    AccessoireDTO toDto(Accessoire accessoire);
    @Mapping(target = "accessoireId",source = "id")
    @Mapping(target = "vehicule.vehiculeId",source = "vehicule.id")
    Accessoire fromDto(AccessoireDTO accessoireDTO);
}
