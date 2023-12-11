package br.ufsm.csi.pilacoin23.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@Table
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] chaveUsuarioOrigem;
    private byte[] chaveUsuarioDestino;
    private String noncePila;  // utilizar precisão de 128 bits
    @Transient
    private Date dataTransacao;
    private String nomeUsuarioOrigem;
    private String nomeUsuarioDestino;
    private byte[] assinatura;
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bloco_id")
    private Bloco bloco;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bloco_validado_id")
    private BlocoValidado blocoValidado;
}
