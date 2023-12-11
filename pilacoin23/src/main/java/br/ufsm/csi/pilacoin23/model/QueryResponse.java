package br.ufsm.csi.pilacoin23.model;

import br.ufsm.csi.pilacoin23.Persistencia.QueryResponsePilaRepositorio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
        Long idQuery;
        String usuario;
        List<QueryResponsePila> pilasResult;
        List<Bloco> blocosResult;
        List<Usuario> usuariosResult;
    }

