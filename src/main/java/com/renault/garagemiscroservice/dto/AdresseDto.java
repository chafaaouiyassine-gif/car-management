package com.renault.garagemiscroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdresseDto {
    private Long id;
    private int numero;
    private String rue;
    private String ville;
    private String pays;
}
