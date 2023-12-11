package br.ufsm.csi.pilacoin23.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryRequest implements Serializable {

    private Long idQuery;
    private String nomeUsuario;
    private TypeQuery tipoQuery;
    private PilaCoinJson.Status statusPila;
    private String usuarioMinerador;
    private String usuario;
    //private String nonce;
    //private Long idBloco;
    //private List<Usuario> usuariosResult;
    //private List<PilaCoinJson> pilasResult;
    //private List<Bloco> blocosResult;
    public enum TypeQuery{
        USUARIOS, PILA, BLOCO
    }

    public QueryRequest( String nomeUsuario, TypeQuery tipoQuery, PilaCoinJson.Status statusPila, String usuarioMinerador) {
        this.idQuery = idQuery;
        //this.usuario = usuario;
        this.nomeUsuario = nomeUsuario;
        this.tipoQuery = tipoQuery;
        this.statusPila = statusPila;
        this.usuarioMinerador = usuarioMinerador;
    }
}
