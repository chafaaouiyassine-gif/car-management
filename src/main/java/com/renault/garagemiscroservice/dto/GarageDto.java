package com.renault.garagemiscroservice.dto;

import static com.renault.garagemiscroservice.utils.MessageGlobale.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GarageDto {
    private Long id;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String name;
    @NotNull(message = VARIABLE_NOT_NULL_MESSAGE)
    private AdresseDto address;
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",message = "Numéro de téléphone est  pas valide")
    private String telephone;
    @Email(message = EMAIL_NOT_VALIDE_MESSAGE)
    @NotBlank(message = VARIABLE_NOT_NULL_MESSAGE)
    private String email;
    @NotEmpty
    private List<HoraireOvertureDto> horaireOvertureList;
}
