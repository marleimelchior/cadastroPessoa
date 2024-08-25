package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @Column(name = "co_uf")
    @NotNull(message = "O uf é obrigatório")
    @NotBlank(message = "O uf não pode ser vazio")
    private String uf;

    @Column(name = "ds_cidade")
    @NotNull(message = "A cidade é obrigatória")
    @NotBlank(message = "A cidade não pode ser vazia")
    private String cidade;

    @Column(name = "ds_bairro", nullable = false)
    @NotNull(message = "O bairro é obrigatório")
    @NotEmpty(message = "O bairro não pode ser vazio")
    private String bairro;

    @Column(name = "ds_logradouro")
    @NotNull(message = "O logradouro é obrigatório")
    @NotEmpty(message = "O logradouro não pode ser vazio")
    private String logradouro;

    @Column(name = "ds_numero", nullable = false)
    @NotNull(message = "O número é obrigatório")
    @NotEmpty(message = "O número não pode ser vazio")
    private String numero;

    @Column(name = "co_cep", nullable = false)
    @NotNull(message = "O cep é obrigatório")
    @NotEmpty(message = "O cep não pode ser vazio")
    private String cep;

    @Column(name = "ds_complemento")
    private String complemento;

    @Column(name = "co_tipo")
    @NotNull(message = "O tipo é obrigatório")
    @NotEmpty(message = "O tipo não pode ser vazio")
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    @JsonBackReference
    private Pessoa pessoa;

}
