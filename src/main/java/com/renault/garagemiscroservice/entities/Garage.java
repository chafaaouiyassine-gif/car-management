package com.renault.garagemiscroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "garage-table")
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "garage_id")
    private Integer garageId;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    private Adresse address;
    private String telephone;
    private String email;
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    private List<HoraireOverture> horaireOvertureList;
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehicule> vehicules;
}
