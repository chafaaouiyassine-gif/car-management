package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.AdresseDto;
import com.renault.garagemiscroservice.entities.Adresse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdresseMapper {
    @Mapping(source = "adresseId",target = "id")
    AdresseDto toDto(Adresse adresse);
    @Mapping(target = "adresseId",source = "id")
    Adresse fromDto(AdresseDto adresseDto);
}
