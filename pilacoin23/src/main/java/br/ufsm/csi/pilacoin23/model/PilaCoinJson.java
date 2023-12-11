package br.ufsm.csi.pilacoin23.model;

import br.ufsm.csi.pilacoin23.interfaces.PilaCoinJsonInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "pilaCoinJson")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaCoinJson implements Cloneable, Serializable, PilaCoinJsonInterface {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private Date dataCriacao;
    private byte[] chaveCriador;
    private String nomeCriador;
    private String nonce;  //utilizar precisão de 128 bits
    private Status status;
    @Transient
    private List<Transacao> transacoes;

    public enum Status{
            VALIDO
    }

    public Long getId() {
        return id;
    }
}
