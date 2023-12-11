package br.ufsm.csi.pilacoin23.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alert")
@CrossOrigin(origins = "*")
public class AlertController {

    @MessageMapping("/show")
    @SendTo("/topic/alerts")
    public void showAlert(@Payload String payload) {
        System.out.println("Payload recebido em showAlert: " + payload);
    }
}
