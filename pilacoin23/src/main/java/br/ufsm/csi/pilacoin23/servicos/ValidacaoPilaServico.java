package br.ufsm.csi.pilacoin23.servicos;

import br.ufsm.csi.pilacoin23.Persistencia.PilaValidadoRepositorio;
import br.ufsm.csi.pilacoin23.model.Feedback;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import br.ufsm.csi.pilacoin23.decoratorLogger.ValidaPilasLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


@Service
public class ValidacaoPilaServico {

    @Autowired
    private PilaValidadoRepositorio pilaValidadoRepositorio;
    @Autowired
    private ValidaPilasLogger validaPilas;

    private static BigInteger DIFICULDADE;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Ouvinte RabbitMQ para a fila "pila-minerado"
    @RabbitListener(queues = {"pila-minerado"})
    public void validarPila(@Payload String strPilacoin) throws Exception {

        // Verifica se a mensagem não está vazia
        if (!strPilacoin.isEmpty()) {
            System.out.println("[pila-valida] - Message: PM " + strPilacoin);

                // Obtém a dificuldade atual do sistema
                DIFICULDADE = new LeDificuldade().retornaDificuldade();

            PilaValidado pilaValidado = validaPilas.validarPilas(strPilacoin, DIFICULDADE);

                // Converte o objeto PilaValidado para JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String pilaValidadoJson = objectMapper.writeValueAsString(pilaValidado);
            // Verifica se pilaValidado é diferente de null
            if (pilaValidado != null) {
                System.out.println("[pila-valida] - Validou $$$");

                // Construa a mensagem de feedback
                String msg = "[pila-valida] - Validou uma PilaCoin!";
                Feedback feedback = new Feedback(msg);

                // Enviar feedback para o tópico WebSocket
                messagingTemplate.convertAndSend("/topic/feedback-cliente", feedback);

                // Enviar o JSON para a fila "pila-validado"
                rabbitTemplate.convertAndSend("pila-validado", pilaValidadoJson);
                System.out.println("[pila-valida] - enviou pila valido ....");
                // salva na tabela "pila-validado"

                pilaValidadoRepositorio.save(pilaValidado);
                System.out.println("                            " + pilaValidadoJson);
            } else {
                System.out.println("");
                System.out.println("[pila-valida] - Nao validou $$$");

                // Se a validação falhar, envia a transação de volta para a fila "pila-minerado"
                //rabbitTemplate.convertAndSend("pila-minerado", strPilacoin);
                System.out.println("[pila-valida] - enviou pila de volta ....");
                System.out.println("                            " + strPilacoin);
            }
        }
    }

}

