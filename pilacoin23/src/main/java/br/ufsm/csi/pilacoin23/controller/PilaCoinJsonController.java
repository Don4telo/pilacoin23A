package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.PilaRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pilaCoin")
public class PilaCoinJsonController {

    @Autowired
    PilaRepositorio pilaRepositorio;

    // Endpoint para salvar um PilaCoinJson via HTTP POST
    @PostMapping("/save")
    public ResponseEntity<PilaCoinJson> savePilacoin(@RequestBody PilaCoinJson pilaCoinJson) {
        PilaCoinJson savedPilaCoin = pilaRepositorio.save(pilaCoinJson);
        return ResponseEntity.ok(savedPilaCoin);
    }

    // Endpoint para obter todos os PilaCoinJson via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity<List<PilaCoinJson>> getAllPilacoins() {
        List<PilaCoinJson> pilacoins = pilaRepositorio.findAll();
        return ResponseEntity.ok(pilacoins);
    }

    // Endpoint para obter todos os PilaCoinJson onde o usuário minerador é "ADonato" via HTTP GET
    @GetMapping("/getAllByUseralexandre")
    public ResponseEntity<List<PilaCoinJson>> getAllPilacoinsByUserADonato() {
        List<PilaCoinJson> pilacoins = pilaRepositorio.findBynomeCriador("alexandre");
        return ResponseEntity.ok(pilacoins);
    }
}

