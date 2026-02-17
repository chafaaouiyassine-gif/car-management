package com.renault.garagemiscroservice.dto;

import static com.renault.garagemiscroservice.utils.MessageGlobale.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessoireDTO {
    private Integer id;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String nom;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String description;
    @NotNull(message = VARIABLE_NOT_NULL_MESSAGE)
    @Positive(message =VARIABLE_POSITIVE_MESSAGE)
    private Float prix;
    @NotBlank(message =VARIABLE_NOT_NULL_MESSAGE)
    private String type;
    private VehiculeDto vehicule;
}
