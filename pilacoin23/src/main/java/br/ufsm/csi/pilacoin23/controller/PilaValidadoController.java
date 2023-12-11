package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.PilaValidadoRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pilaValidado")
public class PilaValidadoController {

    @Autowired
    PilaValidadoRepositorio pilaValidadoRepositorio;

    // Endpoint para salvar um PilaValidado via HTTP POST
    @PostMapping("/save")
    public ResponseEntity<PilaValidado> savePilaValidado(@RequestBody PilaValidado pilaValidado) {
        PilaValidado savedPilaValidado = pilaValidadoRepositorio.save(pilaValidado);
        return ResponseEntity.ok(savedPilaValidado);
    }

    // Endpoint para obter todos os PilaValidado via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity<List<PilaValidado>> getAllPilaValidados() {
        List<PilaValidado> pilaValidados = pilaValidadoRepositorio.findAll();
        return ResponseEntity.ok(pilaValidados);
    }

    // Endpoint para obter todos os PilaValidado com valores correspondentes de PilaCoinJson via HTTP GET
    @GetMapping("/getAllWithPilaCoinJson")
    public ResponseEntity<List<Object[]>> getAllPilaValidadoWithPilaCoinJson() {
        List<Object[]> pilaValidadoWithPilaCoinJson = pilaValidadoRepositorio.findAllPilaValidadoWithPilaCoinJson();
        return ResponseEntity.ok(pilaValidadoWithPilaCoinJson);
    }


}
    /*
    // Endpoint para obter todos os PilaValidado com nonces correspondentes via HTTP GET
    @GetMapping("/getAllWithNonce")
    public ResponseEntity<List<Object[]>> getAllPilaValidadosWithNonce() {
        List<Object[]> pilaValidadosWithNonce = pilaValidadoRepositorio.findAllWithNonce();
        return ResponseEntity.ok(pilaValidadosWithNonce);
    }

      // Endpoint para obter todos os registros da visualização pila_validei via HTTP GET
    @GetMapping("/getAllFromView")
    public ResponseEntity<List<PilaValidado>> getAllFromView() {
        List<PilaValidado> pilaValidadosFromView = pilaValidadoRepositorio.findAllFromView();
        return ResponseEntity.ok(pilaValidadosFromView);
    }

    */

    /*
    SELECT
    pv.id AS pila_validado_id,
    pv.assinatura_pila_coin,
    pv.chave_publica_validador,
    pv.nome_validador,
    pv.pilacoin_json,
    pv.pila_id,
    pcj.id AS pila_coin_json_id,
    pcj.chave_criador,
    pcj.data_criacao,
    pcj.nome_criador,
    pcj.nonce
FROM
    public.pila_validado pv
JOIN
    public.pila_coin_json pcj ON pv.pila_id = pcj.id;




     SELECT pv.id AS pila_validado_id,
    pcj.id AS pila_coin_json_id,
    pcj.data_criacao,
    pcj.nome_criador,
    pcj.nonce
   FROM pila_validado pv
     JOIN pila_coin_json pcj ON pv.pila_id = pcj.id;

     */
