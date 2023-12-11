package br.ufsm.csi.pilacoin23.dtos;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record PilaCoinJsonDto(@NotNull Date dataCriacao, @NotNull String nomeCriador, @NotNull String nonce ) {
}

/*
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Date dataCriacao;
    private byte[] chaveCriador;
    private String nomeCriador;
    private String nonce;  //utilizar precisão de 128 bits
 */