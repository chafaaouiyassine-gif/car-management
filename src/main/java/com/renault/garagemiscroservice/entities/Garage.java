package com.renault.garagemiscroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "garage-table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private int countVehicule;
    private String email;
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    private List<HoraireOverture> horaireOvertureList;
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    @JsonIgnore
    @Size(max = 50)
    private List<Vehicule> vehicules;
}
