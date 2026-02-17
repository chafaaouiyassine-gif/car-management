package com.renault.garagemiscroservice.utils;

import com.renault.garagemiscroservice.dto.AdresseDto;
import com.renault.garagemiscroservice.dto.GarageDto;
import com.renault.garagemiscroservice.dto.HoraireOvertureDto;
import com.renault.garagemiscroservice.dto.OpeningTimeDto;
import com.renault.garagemiscroservice.enums.DayOfWeek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerateValueToTest {
    public static GarageDto generateGarageDtoForCreate(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .startyTime(LocalDate.now())
                .endTime(LocalDate.now())
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
    public static List<GarageDto> generateMultiGarageDtoForCreate(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .startyTime(LocalDate.now())
                .endTime(LocalDate.now())
                .build();
        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTimeDto))
                .build();
        List<GarageDto> garagesToSave=new ArrayList<>();
        AdresseDto luxAdresseDto=AdresseDto.builder()
                .id(null)
                .numero(10)
                .rue("joseph junck")
                .ville("Luxembourg")
                .pays("Luxembourg").build();
        AdresseDto suisAdresseDto=AdresseDto.builder()
                .id(null)
                .numero(50)
                .rue("normandie")
                .ville("Suisse")
                .pays("Suisse").build();
        AdresseDto marAdresseDto=AdresseDto.builder()
                .id(null)
                .numero(41)
                .rue("jabal kotama")
                .ville("Settat")
                .pays("Maroc").build();
        garagesToSave.add( GarageDto.builder().name("abisco")
                .address(luxAdresseDto)
                .email("batsite@gmail.com")
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build());
        garagesToSave.add( GarageDto.builder().name("bilora")
                .address(suisAdresseDto)
                .email("stephane@gmail.com")
                .telephone("0854521694")
                .horaireOvertureList(List.of(horaireOvertureDto)).build());
        garagesToSave.add( GarageDto.builder().name("zibola")
                .address(marAdresseDto)
                .email("nadia@gmail.com")
                .telephone("045872519")
                .horaireOvertureList(List.of(horaireOvertureDto)).build());

        return garagesToSave;
    }
    public static GarageDto generateGarageDtoForUpdate(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .id(1L)
                .startyTime(LocalDate.now())
                .endTime(LocalDate.now())
                .build();
        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTimeDto))
                .build();
        AdresseDto marAdresseDto=AdresseDto.builder()
                .id(null)
                .numero(41)
                .rue("jabal kotama")
                .ville("Settat")
                .pays("Maroc").build();
        return  GarageDto.builder().id(1L).name("garage 1")
                .address(marAdresseDto)
                .email("test@gmail.com")
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build();
    }
    public static GarageDto generateGarageDtoForUpdateNotValideAddress(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .id(1L)
                .startyTime(LocalDate.now())
                .endTime(LocalDate.now())
                .build();
        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTimeDto))
                .build();

        return  GarageDto.builder().id(1L).name("garage 1")
                .email("test@gmail.com")
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build();
    }
    public static GarageDto generateGarageDtoForCreateNotValideEmail(){
        OpeningTimeDto openingTimeDto=OpeningTimeDto.builder()
                .startyTime(LocalDate.now())
                .endTime(LocalDate.now())
                .build();
        HoraireOvertureDto horaireOvertureDto=HoraireOvertureDto.builder()
                .dayOfWeek(DayOfWeek.LUNDI).
                openingTimeList(List.of(openingTimeDto))
                .build();
        AdresseDto marAdresseDto=AdresseDto.builder()
                .id(null)
                .numero(41)
                .rue("jabal kotama")
                .ville("Settat")
                .pays("Maroc").build();
        return  GarageDto.builder().name("garage 1")
                .address(marAdresseDto)
                .email(null)
                .telephone("0668795246")
                .horaireOvertureList(List.of(horaireOvertureDto)).build();
    }

}
