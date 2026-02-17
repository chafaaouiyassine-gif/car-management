package com.renault.garagemiscroservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "opening-time")
@Data
public class OpeningTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long openingTimeId;
    private LocalDate startyTime;
    private LocalDate endTime;
    @ManyToOne
    @JoinColumn(name = "horaire_ouverture_id")
    @ToString.Exclude
    private HoraireOverture horaireOverture;
}
