package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.BlocoValidadoRepositorio;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blocosValidados")
public class BlocoValidadoController {

    @Autowired
    BlocoValidadoRepositorio blocoValidadoRepositorio;


    // Endpoint para salvar um PilaValidado via HTTP POST
    @PostMapping("/save")
    public ResponseEntity<BlocoValidado> saveBlocoValidado(@RequestBody BlocoValidado blocoValidado) {
        BlocoValidado savedBlocoValidado = blocoValidadoRepositorio.save(blocoValidado);
        return ResponseEntity.ok(savedBlocoValidado);
    }

    // Endpoint para obter todos os PilaValidado via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity<List<BlocoValidado>> getAllBlocoValidados() {
        List<BlocoValidado> blocoValidados = blocoValidadoRepositorio.findAll();
        return ResponseEntity.ok(blocoValidados);
    }

    // Endpoint para obter dados de bloco e bloco validado
    @GetMapping("/getAllWithBlock")
    public ResponseEntity<List<Object[]>> getAllBlocoValidadoWithBlock() {
        List<Object[]> blocoValidadoWithBlock = blocoValidadoRepositorio.findAllWithBlock();
        return ResponseEntity.ok(blocoValidadoWithBlock);
    }
}
