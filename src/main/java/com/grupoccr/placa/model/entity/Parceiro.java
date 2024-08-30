package com.grupoccr.placa.model.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Hidden
@Schema(hidden = true)
@Table(name = "tb_parceiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parceiro {

    @Id
    @SequenceGenerator( name="PARCEIRO_SEQUENCE_GENERATOR", sequenceName="SEQ_PARCEIRO_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PARCEIRO_SEQUENCE_GENERATOR")
    @Column(name = "id_parceiro")
    private Long id;
    @Column(name = "ds_nome", nullable = false)
    private String nome;
    @Column(name = "co_codigo", nullable = false)
    private String codigo;

    @Column(name = "co_api_key", unique = true)
    private String apiKey;

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.ALL)
    private List<ClienteTelefone> telefones;

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.ALL)
    private List<ClienteEmail> emails;

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.ALL)
    private List<ClienteEndereco> enderecos;

    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.ALL)
    private List<Placa> placas;

}
