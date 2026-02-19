package com.renault.garagemiscroservice.entities;

import com.renault.garagemiscroservice.enums.TypeCarburant;
import com.renault.garagemiscroservice.enums.TypeVehicule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "vehicule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicule_id")
    private Integer vehiculeId;
    private String brand;
    private String model;
    private String anneeFabrication;
    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;
    @Enumerated(EnumType.STRING)
    private TypeVehicule typeVehicule;
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    private List<Accessoire> accessoires;

}
