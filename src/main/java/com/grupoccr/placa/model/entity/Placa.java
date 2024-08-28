package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "tb_placa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Placa {

    @Id
    @SequenceGenerator( name="PLACA_SEQUENCE_GENERATOR", sequenceName="SEQ_PLACA_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PLACA_SEQUENCE_GENERATOR")
    @Column(name = "id_placa")
    private Long id;

    @Column(name = "ds_placa")
    private String placa;


    @Enumerated(EnumType.STRING)
    @Column(name = "st_ativo")
    private StatusAtivo ativo;

    @ManyToOne
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    @JsonBackReference
    private Pessoa pessoa;

    public void ativarDesativar(boolean ativo) {
        this.ativo = ativo ? StatusAtivo.S : StatusAtivo.N;
    }

}
