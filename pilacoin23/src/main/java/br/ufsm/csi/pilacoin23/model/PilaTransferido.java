package br.ufsm.csi.pilacoin23.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaTransferido implements Serializable {

    private byte[] chaveUsuarioOrigem;
    private byte[] chaveUsuarioDestino;

    private String nomeUsuarioOrigem;
    private String nomeUsuarioDestino;
    private byte[] assinatura;  //assinatura deste objeto pelo usuário de origem
    private String nonce;  //utilizar precisão de 128 bits
    private Date dataTransacao;
    //private Long id;
    //@JsonIgnore
    //private String status;

}
