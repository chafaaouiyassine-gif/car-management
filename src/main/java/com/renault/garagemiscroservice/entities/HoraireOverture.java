package com.renault.garagemiscroservice.entities;

import com.renault.garagemiscroservice.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "horaire-ouverture")
public class HoraireOverture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horaire_ouverture_id")
    private Integer horaireOuvertureId;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @OneToMany(mappedBy = "horaireOverture")
    private List<OpeningTime> openingTimeList;
    @ManyToOne
    @JoinColumn(name = "garage_id")
    @ToString.Exclude
    private Garage garage;
}
