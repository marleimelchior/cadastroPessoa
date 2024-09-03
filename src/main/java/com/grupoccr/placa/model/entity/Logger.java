package com.grupoccr.placa.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Hidden
@Schema(hidden = true)
@Table(name = "tb_logger")
@Data
@NoArgsConstructor
public class Logger {

    @Id
    @SequenceGenerator(name = "LOGGER_SEQUENCE_GENERATOR", sequenceName = "SEQ_LOGGER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGGER_SEQUENCE_GENERATOR")
    @Column(name = "CO_SEQ_IDLOGGER")
    private Long id;

    @Column(name = "CO_UUID")
    private String uuid;

    @Column(name = "DT_REQUEST")
    private Timestamp dataRequest;

    @Column(name = "DT_TIME_REQUEST")
    private BigDecimal timeRequest;

    @Column(name = "DS_TYPE")
    private String type;

    @Column(name = "DS_URL")
    private String url;

    @Column(name = "DS_STATUS_CODE")
    private Integer statusCode;

    @Column(name = "DS_HEADER", columnDefinition = "text")
    private String header;

    @Column(name = "DS_REQUEST", columnDefinition = "text")
    private String request;

    @Column(name = "DS_RESPONSE", columnDefinition = "text")
    private String response;

}
