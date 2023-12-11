package br.ufsm.csi.pilacoin23.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaValidado {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String nomeValidador;
    private byte[] chavePublicaValidador;
    private byte[] assinaturaPilaCoin;
    //private String nonce;

    // Mapeamento ManyToOne com referência à coluna 'pilaCoinJson_id' na tabela PilaValidado
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pilaCoinJson_id", referencedColumnName = "id")
    private PilaCoinJson pilaCoinJson;

}
