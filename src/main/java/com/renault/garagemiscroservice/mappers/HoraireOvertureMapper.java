package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.HoraireOvertureDto;
import com.renault.garagemiscroservice.entities.HoraireOverture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HoraireOvertureMapper {
    @Mapping(source = "horaireOuvertureId",target = "id")
    HoraireOvertureDto toDto(HoraireOverture horaireOverture);
    @Mapping(target = "horaireOuvertureId",source = "id")
    HoraireOverture fromDto(HoraireOvertureDto horaireOvertureDto);
}
