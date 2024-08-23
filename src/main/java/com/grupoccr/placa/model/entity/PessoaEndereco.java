package com.grupoccr.placa.model.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Hidden
@Schema(hidden = true)
@Table(name = "tb_pessoa_endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaEndereco {

    @Id
    @SequenceGenerator( name="PESSOA_ENDERECO_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_ENDERECO_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_ENDERECO_SEQUENCE_GENERATOR")
    @Column(name = "id_pessoa_endereco")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_parceiro", nullable = false)
    private Parceiro parceiro;

    @Column(name = "co_uf")
    private String uf;

    @Column(name = "ds_cidade")
    private String cidade;

    @Column(name = "ds_bairro")
    private String bairro;

    @Column(name = "ds_logradouro")
    private String logradouro;

    @Column(name = "ds_numero")
    private String numero;

    @Column(name = "co_cep")
    private String cep;

    @Column(name = "ds_complemento")
    private String complemento;

    @Column(name = "co_tipo")
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

}
