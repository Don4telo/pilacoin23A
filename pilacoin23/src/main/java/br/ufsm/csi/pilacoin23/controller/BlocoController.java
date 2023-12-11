package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.BlocoRepositorio;
import br.ufsm.csi.pilacoin23.model.Bloco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blocos")
public class BlocoController {

    @Autowired
    BlocoRepositorio blocoRepositorio;

    // Endpoint para salvar um Bloco via HTTP POST
    @PostMapping("/save")
    public ResponseEntity<Bloco> saveBloco(@RequestBody Bloco bloco) {
        Bloco savedBloco = blocoRepositorio.save(bloco);
        return ResponseEntity.ok(savedBloco);
    }

    // Endpoint para obter todos os Blocos via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity<List<Bloco>> getAllBlocos() {
        List<Bloco> blocos = blocoRepositorio.findAll();
        return ResponseEntity.ok(blocos);
    }

    // Endpoint para obter todos os Blocos do usuário 'ADonato' via HTTP GET
    @GetMapping("/getMeusBlocos")
    public ResponseEntity<List<Bloco>> getMeusBlocos() {
        // Aqui usamos o método personalizado findByNomeUsuarioMinerador da interface do repositório
        List<Bloco> blocos = blocoRepositorio.findByNomeUsuarioMinerador("alexandre");
        return ResponseEntity.ok(blocos);
    }
}

