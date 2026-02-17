package com.renault.garagemiscroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Entity
@Table(name = "adresse")
@Data
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adresse_id")
    private Integer adresseId;
    private int numero;
    private String rue;
    private String ville;
    private String pays;
    @OneToOne(mappedBy = "address")
    @ToString.Exclude
    @JsonIgnore
    private Garage garage;
}
