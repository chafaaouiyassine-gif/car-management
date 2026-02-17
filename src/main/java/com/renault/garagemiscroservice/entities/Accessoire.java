package com.renault.garagemiscroservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "accessoire")
@Data
public class Accessoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accessoire_id")
    private Integer accessoireId;
    private String nom;
    private String description;
    private float prix;
    private String type;
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    @ToString.Exclude
    private Vehicule vehicule;
}
