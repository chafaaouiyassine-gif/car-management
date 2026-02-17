package com.renault.garagemiscroservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "opening-time")
@Data
public class OpeningTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer openingTimeId;
    private LocalTime startyTime;
    private LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "horaire_ouverture_id")
    @ToString.Exclude
    private HoraireOverture horaireOverture;
}
