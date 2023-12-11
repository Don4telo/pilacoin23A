package br.ufsm.csi.pilacoin23.servicos;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ReportServico {

    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "report")
    public void receiveMsgFeedbackUser(@Payload String feedback) {

        try {
            System.out.println("[report] - Mensagem de feedback recebida: "+ feedback);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao receber mensagem de feedback! ", e);
        }
    }
}

/*
 . *[report] - Mensagem de feedback recebida: [
 {"geradoEm":1701557823528,"nomeUsuario":"donato","minerouPila":true,"validouPila":true,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
 {"geradoEm":1701557823528,"nomeUsuario":"ewerton-joaokunde","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
 {"geradoEm":1701557823528,"nomeUsuario":"ARDonato","minerouPila":true,"validouPila":true,"minerouBloco":false,"validouBloco":false,"transferiuPila":false},
 {"geradoEm":1701557823528,"nomeUsuario":"ADonato","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false},
 {"geradoEm":1701557823528,"nomeUsuario":"ADonato2","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":false,"transferiuPila":false}
 ,{"geradoEm":1701557823528,"nomeUsuario":"alexandre","minerouPila":true,"validouPila":true,"minerouBloco":true,"validouBloco":true,"transferiuPila":false},
*/
/*  IMPLEMENTAR
        Mensagem enviada fila: query, mensagem: {
        "idQuery": 201,
        "nomeUsuario": "Augusto",
        "nonce": "61360437041797561799859779655774669076238166321139867284012704975865610550484",
        "tipoQuery": "PILA"
        }

        Enviar a query para a queue "query". A resposta será enviada para "${user}-query" com o mesmo idQuery.
        Estrutura:

        {
        "idQuery": numero, //qualquer numero! Será o mesmo enviado na resposta
        "nomeUsuario": "nomedousuario", //serve para identificar a fila para a qual mandar a resposta
        // os dados a seguir são relativos à própria query
        "tipoQuery": "PILA|BLOCO|USUARIOS", //um dos três para identificar qual o tipo de objeto buscar
        "status": "status", //Strings de um dos status de PILA ou BLOCO para filtrar. Não obrigatório.
        "usuarioMinerador": "nomeusuario", //filtrar blocos ou pilas pelo usuário minerador. Não obrigatório.
        "nonce": "string", //filtrar um pila ou bloco pelo seu nonce. Não obrigatório.
        "idBloco": numero //filtrar o bloco pelo seu numero. Não obrigatório.

        }


        Resposta da Query:

        {
        "idQuery": numero //mesmo da query original
        "usuario": "string" //mesmo nomeUsuario da query original
        "pilasResult": [...], //resultado caso a query seja do tipo "PILA"
        "blocosResult": [...], //resultado caso a query seja do tipo "BLOCO"
        "usuariosResult": [...] //resultado caso a query seja do tipo "USUARIOS"
        }
        */