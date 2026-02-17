package com.renault.garagemiscroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class OpeningTimeDto {
    private Integer id;
    private LocalTime startyTime;
    private LocalTime endTime;
}
