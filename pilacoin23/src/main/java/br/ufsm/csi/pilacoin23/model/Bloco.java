package br.ufsm.csi.pilacoin23.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table
public class Bloco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long id;

    private Long numeroBloco;
    private String nonce;  // utilizar precisão de 128 bits
    private String nonceBlocoAnterior;
    private String nomeUsuarioMinerador;
    private byte[] chaveUsuarioMinerador;

    @OneToMany(mappedBy = "bloco", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;
}
