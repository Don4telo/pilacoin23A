package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.QueryResponsePilaRepositorio;
import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.model.QueryResponsePila;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pilasServidor")
public class QueryResponsePilaController {

    @Autowired
    QueryResponsePilaRepositorio queryResponsePilaRepositorio;

    // Endpoint para obter todos os Blocos via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity getAllpilasServidor() {
        List<QueryResponsePila> queryResponsePilas = queryResponsePilaRepositorio.findAll();
        return ResponseEntity.ok(queryResponsePilas);
    }

    // Endpoint para salvar todos os registros de QueryResponsePila via HTTP POST
    @PostMapping("/saveAll")
    public ResponseEntity<List<QueryResponsePila>> saveAllPilas(@RequestBody List<QueryResponsePila> pilas) {
        // Salva todas as pilas no repositório
        queryResponsePilaRepositorio.saveAll(pilas);
        return ResponseEntity.ok(pilas);
    }

}
