package br.ufsm.csi.pilacoin23.semUso;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//@Service
public class ErrorsServico {

    @Autowired
    private RabbitTemplate rabbitTemplate;
   //@RabbitListener(queues = {"ADonato-query"})
    public void receiveErrors(@Payload String strErrors) {
        System.out.println("Message: err " + strErrors);

    }

}

/*
@@@ Mensagem de feedback recebida: [
{"geradoEm":1701283283118,"nomeUsuario":null,"minerouPila":false,"validouPila":false,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Christian Alves","minerouPila":false,"validouPila":false,"minerouBloco":false,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Matheus","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Luiz Felipe","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"luizf","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"Gabriel_Valentim","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"Joao Vitor Oliveira","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"Vitor Fraporti","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"christian fagundes","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Casanova","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"ewerton-joaokunde","minerouPila":true,"validouPila":true,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"christian alves","minerouPila":true,"validouPila":false,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"joao vitor","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Augusto","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"ADonato","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"joao_leo","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"Jo�o Vitor Oliveira","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Gabriel Valentim","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Casannova","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"londeroedu","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"ADonato2","minerouPila":false,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"biancamagro","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"rmilbradt","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"Cristian Schmitzhaus","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"fraporti","minerouPila":true,"validouPila":true,"minerouBloco":false,"validouBloco":true,"transferiuPila":true},
{"geradoEm":1701283283118,"nomeUsuario":"bilar-pedro","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"christian_alves","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
{"geradoEm":1701283283118,"nomeUsuario":"joao_vitor","minerouPila":true,"validouPila":false,"minerouBloco":false,"validouBloco":false,"transferiuPila":false}]

 */