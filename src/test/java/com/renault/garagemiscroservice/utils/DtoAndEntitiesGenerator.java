package com.renault.garagemiscroservice.utils;

import com.renault.garagemiscroservice.dto.*;
import com.renault.garagemiscroservice.entities.*;
import com.renault.garagemiscroservice.enums.DayOfWeek;
import com.renault.garagemiscroservice.enums.TypeCarburant;
import com.renault.garagemiscroservice.enums.TypeVehicule;

import java.time.LocalTime;
import java.util.List;

public class DtoAndEntitiesGenerator {
    public static GarageDto generateGarageDto(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .startyTime(LocalTime.MIN)
                .endTime(LocalTime.MAX)
                .build();
        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTimeDto))
                .build();

        AdresseDto adresseDto=AdresseDto.builder()
                                         .id(null)
                                          .numero(14)
                                          .rue("jabal kotama")
                                          .ville("Settat")
                                          .pays("Maroc").build();

        return  GarageDto.builder().name("garage 1")
                .address(adresseDto)
                .email("test@gmail.com")
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build();
    }
    public static Garage generateGarageEntity(){
        OpeningTime openingTime=OpeningTime.builder()
                .openingTimeId(1)
                .startyTime(LocalTime.MIN)
                .endTime(LocalTime.MAX)
                .build();
        HoraireOverture horaireOvertureDto=HoraireOverture.builder()
                .horaireOuvertureId(1)
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTime))
                .build();

        Adresse adresseDto=Adresse.builder()
                .adresseId(1)
                .numero(14)
                .rue("jabal kotama")
                .ville("Settat")
                .pays("Maroc").build();

        return  Garage.builder().name("garage 1")
                .address(adresseDto)
                .email("test@gmail.com")
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build();
    }

    public static VehiculeDto generateVehiculeDto(){
        GarageDto garageDto=generateGarageDto();
        garageDto.setId(1);
        return VehiculeDto.builder()
                .brand("Mercedess")
                .model("2022")
                .typeVehicule(TypeVehicule.VOITURE)
                .anneeFabrication("2021")
                .typeCarburant(TypeCarburant.DIESEL)
                .garage(garageDto)
                .build();
    }
    public static Vehicule generateVehiculeEntity(){
        Garage garage=generateGarageEntity();
        return Vehicule.builder()
                .brand("Mercedess")
                .model("2022")
                .typeVehicule(TypeVehicule.VOITURE)
                .anneeFabrication("2021")
                .typeCarburant(TypeCarburant.DIESEL)
                .garage(garage)
                .build();
    }
    public static AccessoireDTO generateAccessoireDto(){
        VehiculeDto vehiculeDto=generateVehiculeDto();
        return AccessoireDTO.builder()
                .nom("accesseoire 1")
                .description("description 1").
                prix(25.56f)
                .type("test")
                .vehicule(vehiculeDto)
                .build();
    }
}
