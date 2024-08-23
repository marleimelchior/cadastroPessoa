package com.grupoccr.placa.model.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Schema(hidden = true)
@Entity
@Hidden
@Table(name = "tb_pessoa_telefone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaTelefone {

    @Id
    @SequenceGenerator( name="PESSOA_TELEFONE_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_TELEFONE_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_TELEFONE_SEQUENCE_GENERATOR")
    @Column(name = "id_pessoa_telefone")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_parceiro", nullable = false)
    private Parceiro parceiro;

    @Column(name = "cod_ddd", nullable = false)
    private String ddd;

    @Column(name = "tel_numero", nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

}
