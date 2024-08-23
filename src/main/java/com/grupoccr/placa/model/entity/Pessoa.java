package com.grupoccr.placa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @SequenceGenerator( name="PESSOA_SEQUENCE_GENERATOR", sequenceName="SEQ_PESSOA_ID" )
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="PESSOA_SEQUENCE_GENERATOR")
    @Column(name = "id_pessoa")
    private Long id;

    @Column(name = "ds_nome", nullable = false)
    private String nome;

    @Column(name = "ds_cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<PessoaTelefone> telefones;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<PessoaEmail> emails;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<PessoaEndereco> enderecos;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Placa> placas;


    private static final Logger logger = LoggerFactory.getLogger(Pessoa.class);

    // Método de validação de CPF e CNPJ
    public boolean isValidCpfCnpj() {
        if (this.cpfCnpj == null || this.cpfCnpj.isEmpty()) {
            logger.error("CPF/CNPJ não pode ser nulo ou vazio");
            return false;
        }
        if (this.cpfCnpj.length() == 11) {
            logger.info("Validando CPF", this.cpfCnpj);
            return isValidCPF(this.cpfCnpj);
        } else if (this.cpfCnpj.length() == 14) {
            logger.info("Validando CNPJ", this.cpfCnpj);
            return isValidCNPJ(this.cpfCnpj);
        }

        return false;
    }

    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) {
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
            logger.info("CPF válido", cpf);
            return cpf.endsWith(digit1 + "" + digit2);
        } catch (NumberFormatException e) {
            logger.error("Erro ao validar CPF", cpf);
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
            logger.info("CNPJ válido", cnpj);
            return cnpj.endsWith(digit1 + "" + digit2);
        } catch (NumberFormatException e) {
            logger.error("Erro ao validar CNPJ", cnpj);
            return false;
        }
    }
}
