package com.grupoccr.placa.model.entity;

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

    @Column(name = "ds_placa", nullable = false)
    private String placa;

    @Column(name = "ds_cpf_cnpj", nullable = false)
    private String cpfCnpj;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(nullable = false, name = "st_ativo")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_parceiro", nullable = false)
    private Parceiro parceiro;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

}
