package com.renault.garagemiscroservice.mappers;

import com.renault.garagemiscroservice.dto.OpeningTimeDto;
import com.renault.garagemiscroservice.entities.OpeningTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {
    @Mapping(source = "openingTimeId",target = "id")
    OpeningTimeDto toDto(OpeningTime openingTime);
    @Mapping(target = "openingTimeId",source = "id")
    OpeningTime fromDto(OpeningTimeDto openingTimeDto);
}
