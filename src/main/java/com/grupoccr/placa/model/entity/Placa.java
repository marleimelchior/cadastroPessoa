package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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
    @Size(min = 7, max = 7, message = "A placa deve ter exatamente 7 caracteres")
    private String placa;

    @Column(name = "ds_cpf_cnpj")
    private String cpfCnpj;


    @Enumerated(EnumType.STRING)
    @Column(name = "st_ativo")
    private StatusAtivo ativo;

    @Column(name = "dt_data_modificacao")
    private LocalDateTime dataModificacao;

    @ManyToOne
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    @JsonBackReference
    private Cliente cliente;

    @PrePersist
    @PreUpdate
    private void formatPlacaAndDataModificacao() {
        this.placa = this.placa.toUpperCase();
        this.dataModificacao = LocalDateTime.now();
    }

    public void ativarDesativar(boolean ativo) {
        this.ativo = ativo ? StatusAtivo.S : StatusAtivo.N;
    }

}
