package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.Persistencia.UsuarioRepositorio;
import br.ufsm.csi.pilacoin23.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController{
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Endpoint para obter todos os Blocos via HTTP GET
    @GetMapping("/getAll")
    public ResponseEntity<List<Usuario>> getAllUsers() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para salvar todos os usuários via HTTP POST
    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAllUsers(@RequestBody List<Usuario> usuarios) {
        // Salva todos os usuários no repositório
        usuarioRepositorio.saveAll(usuarios);
        return ResponseEntity.ok("Usuários salvos com sucesso");
    }


}
