package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @Column(name = "cod_ddd")
    @NotNull(message = "O ddd é obrigatório")
    @NotEmpty(message = "O ddd não pode ser vazio")
    private String ddd;

    @Column(name = "tel_numero")
    @NotNull(message = "O número é obrigatório")
    @NotEmpty(message = "O número não pode ser vazio")
    @Pattern(regexp = "\\d{9}", message = "O número de telefone deve ter exatamente 9 dígitos")
    private String numero;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    @JsonBackReference
    private Pessoa pessoa;

    @Column(name = "st_ativo")
    private String stAtivo;

}
