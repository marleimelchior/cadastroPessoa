package com.grupoccr.placa.model.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Timestamp timeRequest;

    @Column(name = "DS_TYPE")
    private String type;

    @Column(name = "DS_URL")
    private String url;

    @Column(name = "DS_STATUS_CODE")
    private Integer statusCode;

    @Column(name = "DS_HEADER", length = 4000)
    private String header;
    //lembrando que vem objeto json
    @Column(name = "DS_REQUEST", length = 4000)
    private String request;
    @Column(name = "DS_RESPONSE", length = 4000)
    private String response;

}
