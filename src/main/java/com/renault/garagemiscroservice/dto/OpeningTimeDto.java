package com.renault.garagemiscroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OpeningTimeDto {
    private Long id;
    private LocalDate startyTime;
    private LocalDate endTime;
}
