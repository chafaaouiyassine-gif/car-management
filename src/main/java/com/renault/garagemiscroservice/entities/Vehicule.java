package com.renault.garagemiscroservice.entities;

import com.renault.garagemiscroservice.enums.TypeCarburant;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "vehicule")
@Data
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicule_id")
    private Long vehiculeId;
    private String brand;
    private String model;
    private String anneeFabrication;
    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    private List<Accessoire> accessoires;

}
