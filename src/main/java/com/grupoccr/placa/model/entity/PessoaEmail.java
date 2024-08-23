package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Hidden
@Table(name = "tb_pessoa_email")
@Schema(hidden = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaEmail {

    @Id
    @SequenceGenerator( name="PESSOA_EMAIL_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_EMAIL_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_EMAIL_SEQUENCE_GENERATOR")
    @JsonIgnore
    @Column(name = "id_pessoa_email")
    private Long id;

    @Column(name = "ds_email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_parceiro", nullable = false)
    private Parceiro parceiro;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

}
