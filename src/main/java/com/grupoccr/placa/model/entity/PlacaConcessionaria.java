package com.grupoccr.placa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_placa_concessionaria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacaConcessionaria {

    @Id
    @SequenceGenerator(name = "PLACACONCESSIONARIA_SEQUENCE_GENERATOR", sequenceName = "SEQ_PLACACONCESSIONARIA_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLACACONCESSIONARIA_SEQUENCE_GENERATOR")
    @Column(name = "id_placa_concessionaria")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_placa")
    private Placa placa;

    @Column(name = "co_concessionaria")
    private int codigoConcessionaria;

    @Column(name = "ds_concessionaria")
    private String dsConcessionaria;

    @OneToMany(mappedBy = "placaConcessionaria", cascade = CascadeType.ALL)
    private List<PracaBloqueada> pracasBloqueadas;

}
