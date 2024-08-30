package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Hidden
@Table(name = "tb_pessoa_email")
@Schema(hidden = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEmail {

    @Id
    @SequenceGenerator( name="PESSOA_EMAIL_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_EMAIL_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_EMAIL_SEQUENCE_GENERATOR")
    @JsonIgnore
    @Column(name = "id_pessoa_email")
    private Long id;

    @Column(name = "ds_email")
    @NotNull(message = "O email é obrigatório")
    @NotEmpty(message = "O email não pode ser vazio")
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    @JsonBackReference
    private Cliente cliente;

    @Column(name = "st_ativo")
    private String stAtivo;

}
