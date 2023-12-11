package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.TransacaoRepositorio;
import br.ufsm.csi.pilacoin23.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    @Autowired
    TransacaoRepositorio transacaoRepositorio;

    public ResponseEntity<Transacao> saveTransacao(Transacao transacao){
        return ResponseEntity.ok(transacaoRepositorio.save(transacao));
    }

}
