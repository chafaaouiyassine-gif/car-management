package com.renault.garagemiscroservice.dto;

import com.renault.garagemiscroservice.enums.TypeCarburant;
import static com.renault.garagemiscroservice.utils.MessageGlobale.*;

import com.renault.garagemiscroservice.enums.TypeVehicule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehiculeDto {
    private Integer id;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String brand;
    private String model;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String anneeFabrication;
    @NotNull(message = VARIABLE_NOT_NULL_MESSAGE)
    private TypeCarburant typeCarburant;
    private TypeVehicule typeVehicule;
    private GarageDto garage;
}
