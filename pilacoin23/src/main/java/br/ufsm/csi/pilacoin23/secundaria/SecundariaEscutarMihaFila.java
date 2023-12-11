package br.ufsm.csi.pilacoin23.secundaria;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//@Service
public class SecundariaEscutarMihaFila {
    @Autowired
    RabbitTemplate rabbitTemplate;

       //@RabbitListener(queues = "adonato2")
        public void receiveMsgFeedbackUser(@Payload String feedback) {
            try {
                System.out.println("[servidor2] - XXXXX Mensagem de feedback recebida: "+ feedback);
            } catch (Exception e) {
                throw new RuntimeException("[servidor2] - Erro ao receber mensagem de feedback! ", e);
            }
            //final String mensagem = "{\"idQuery\": \"1001\",\"nomeUsuario\": \"adonato\", \"tipoQuery\": \"PILA\"}";
            //rabbitTemplate.convertAndSend("Adonato-query", mensagem);
           //System.out.println("enviou mensagem = " + mensagem);
        }
}

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