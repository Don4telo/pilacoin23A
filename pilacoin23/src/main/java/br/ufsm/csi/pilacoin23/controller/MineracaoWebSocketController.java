package br.ufsm.csi.pilacoin23.controller;

import br.ufsm.csi.pilacoin23.servicos.MineracaoPilaServico;
import br.ufsm.csi.pilacoin23.util.MineraPilas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/mineracao")
public class MineracaoWebSocketController {

    @Autowired
    private final MineraPilas mineraPilas;

    public MineracaoWebSocketController(MineraPilas mineracaoPilaServico) {
        this.mineraPilas = mineracaoPilaServico;
    }

    @PostMapping("/parar")
    public ResponseEntity<String> pararMineracao(@RequestBody String payload) {
        System.out.println("akkkkkkkki");
        System.out.println(payload);
        mineraPilas.pararMineracao();
        return ResponseEntity.ok("Mineração parada com sucesso!");
    }

    @PostMapping("/reiniciar")
    public ResponseEntity<String> reiniciarMineracao(@RequestBody String payload) {
        System.out.println("alllllllli");
        System.out.println(payload);
        mineraPilas.iniciaMineracao();
        return ResponseEntity.ok("Mineração reiniciada com sucesso!");
    }
}


/*
@Controller
@CrossOrigin("*")
@MessageMapping("/pila_minera")
public class MineracaoWebSocketController {

    // Injeta o serviço de mineração
    @Autowired
    private final MineracaoPilaServico mineracaoPilaServico;

    public MineracaoWebSocketController(MineracaoPilaServico mineracaoPilaServico) {
        this.mineracaoPilaServico = mineracaoPilaServico;
    }

    // Controlador para receber mensagem de parar a mineração via WebSocket

    @SendTo("/topic/feedback-cliente")
    @PostMapping("/parou")
    public FeedbackMessage pararMineracao(@Payload String payload) {
        // Lógica para parar a mineração aqui
        // ...
        System.out.println("akkkkkkkki");
        System.out.println(payload);
        mineracaoPilaServico.pararMineracao();
        // Enviar feedback de sucesso para o tópico /topic/feedback
        return new FeedbackMessage("Mineração parada com sucesso!");
    }

    // Controlador para receber mensagem de reiniciar a mineração via WebSocket
    @SendTo("/topic/feedback-cliente")
    @MessageMapping("/comecou")
    public FeedbackMessage reiniciarMineracao(@Payload String payload) {
        // Lógica para reiniciar a mineração aqui
        // ...
        System.out.println("alllllllli");
        System.out.println(payload);
        mineracaoPilaServico.iniciaMineracao();
        // Enviar feedback de sucesso para o tópico /topic/feedback
        return new FeedbackMessage("Mineração reiniciada com sucesso!");
    }

    // Classe para representar a mensagem de feedback
    public static class FeedbackMessage {
        private String msg;

        public FeedbackMessage(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}

 */

