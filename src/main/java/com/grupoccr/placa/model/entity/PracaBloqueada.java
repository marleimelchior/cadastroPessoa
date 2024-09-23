package com.grupoccr.placa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rl_placaconcession_pracabloque")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracaBloqueada {

    @EmbeddedId
    private PracaBloqueadaId id;

    @ManyToOne
    @MapsId("placaConcessionariaId")
    @JoinColumn(name = "id_placa_concessionaria")
    private PlacaConcessionaria placaConcessionaria;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PracaBloqueadaId implements java.io.Serializable {
        private Long placaConcessionariaId;
        private int codigoPraca;
    }
}
