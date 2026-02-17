package com.renault.garagemiscroservice.dto;

import com.renault.garagemiscroservice.enums.DayOfWeek;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class HoraireOvertureDto {
    private Integer id;
    private DayOfWeek dayOfWeek;
    private List<OpeningTimeDto> openingTimeList;

}
