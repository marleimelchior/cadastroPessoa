package com.grupoccr.placa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "tb_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @SequenceGenerator( name="PESSOA_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_SEQUENCE_GENERATOR")
    @Column(name = "id_pessoa")
    private Long id;

    @Column(name = "ds_nome")
    @NotNull(message = "O nome é obrigatório")
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    @Column(name = "ds_cpf_cnpj", unique = true)
    @NotNull(message = "O cpfCnpj é obrigatório")
    @NotEmpty(message = "O cpfCnpj não pode ser vazio")
    private String cpfCnpj;

    @ManyToOne
    @JoinColumn(name = "id_parceiro")
    private Parceiro parceiro;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<ClienteTelefone> telefones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL) @ToString.Exclude
    private List<ClienteEmail> emails;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<ClienteEndereco> enderecos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Placa> placas;


    private static final Logger logger = LoggerFactory.getLogger(Cliente.class);

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

    // Método de validação de CPF e CNPJ
    public boolean isValidCpfCnpj() {
        if (this.cpfCnpj == null || this.cpfCnpj.isEmpty()) {
            logger.error("CPF/CNPJ não pode ser nulo ou vazio");
            return false;
        }

        int length = this.cpfCnpj.length();
        if (length == 11) {
            logger.info("Validando CPF", this.cpfCnpj);
            return isValidCPF(this.cpfCnpj);
        } else if (length == 14) {
            logger.info("Validando CNPJ", this.cpfCnpj);
            return isValidCNPJ(this.cpfCnpj);
        } else {
            logger.error("O cpfCnpj deve ter 11 ou 14 caracteres", this.cpfCnpj);
            return false;
        }
    }
    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) {
            logger.error("CPF inválido", cpf);
            return false;
        }

        try {
            int sum1 = 0, sum2 = 0, factor1 = 10, factor2 = 11;
            for (int i = 0; i < 9; i++) {
                int digit = Integer.parseInt(cpf.substring(i, i + 1));
                sum1 += digit * factor1--;
                sum2 += digit * factor2--;
            }

            int digit1 = sum1 % 11 < 2 ? 0 : 11 - sum1 % 11;
            sum2 += digit1 * 2;
            int digit2 = sum2 % 11 < 2 ? 0 : 11 - sum2 % 11;
            boolean isValid = cpf.endsWith(digit1 + "" + digit2);
            logger.info("CPF {} válido", isValid ? "é" : "não é", cpf);
            return isValid;
        } catch (NumberFormatException e) {
            logger.error("Erro ao validar CPF", e);
            return false;
        }
    }
    private boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || cnpj.matches(cnpj.charAt(0) + "{14}")) {
            logger.error("CNPJ inválido", cnpj);
            return false;
        }

        try {
            int sum1 = 0, sum2 = 0, factor1 = 5, factor2 = 6;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(cnpj.substring(i, i + 1));
                sum1 += digit * factor1--;
                sum2 += digit * factor2--;

                if (factor1 < 2) factor1 = 9;
                if (factor2 < 2) factor2 = 9;
            }

            int digit1 = sum1 % 11 < 2 ? 0 : 11 - sum1 % 11;
            sum2 += digit1 * 2;
            int digit2 = sum2 % 11 < 2 ? 0 : 11 - sum2 % 11;
            boolean isValid = cnpj.endsWith(digit1 + "" + digit2);
            logger.info("CNPJ {} válido", isValid ? "é" : "não é", cnpj);
            return isValid;
        } catch (NumberFormatException e) {
            logger.error("Erro ao validar CNPJ", e);
            return false;
        }
    }

}
