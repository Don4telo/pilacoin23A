package br.ufsm.csi.pilacoin23.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@Table
public class BlocoValidado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long numeroBloco;
    private String nomeValidador;
    private String nonce;
    private String nonceBlocoAnterior;
    private byte[] chavePublicaValidador;
    private byte[] assinaturaBloco;

    @OneToMany(mappedBy = "blocoValidado", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bloco_id")
    private Bloco bloco;
}

