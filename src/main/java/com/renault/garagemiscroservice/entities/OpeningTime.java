package com.renault.garagemiscroservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "opening-time")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
