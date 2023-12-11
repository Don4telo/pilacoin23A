package br.ufsm.csi.pilacoin23.servicos;

import br.ufsm.csi.pilacoin23.Persistencia.BlocoValidadoRepositorio;
import br.ufsm.csi.pilacoin23.decoratorLogger.ValidaBlocosLogger;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import br.ufsm.csi.pilacoin23.model.Feedback;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.math.BigInteger;

//@Service
public class ValidacaoBlocoServico {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static BigInteger DIFICULDADE;

    @Autowired
    private ValidaBlocosLogger validaBlocos;

    @Autowired
    private BlocoValidadoRepositorio blocoValidadoRepositorio;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @SneakyThrows
    @RabbitListener(queues = {"bloco-minerado"})
    public void validaBloco(@Payload String strBlocoMinerado) {
        System.out.println("");
        System.out.println("[bloco-valida] - Message: BM " + strBlocoMinerado);

        LeDificuldade leDificuldade = new LeDificuldade();
        DIFICULDADE = leDificuldade.retornaDificuldade();

        BlocoValidado blocoValidado = validaBlocos.validarBloco( strBlocoMinerado,  DIFICULDADE);

        if (blocoValidado != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String strBlocoValidado = mapper.writeValueAsString(blocoValidado);
            System.out.println("[bloco-valida] - bloco validado....");
            // Construa a mensagem de feedback
            String msg = "[pila-valida] - Validou uma Bloco!";
            Feedback feedback = new Feedback(msg);

            // Enviar feedback para o tópico WebSocket
            messagingTemplate.convertAndSend("/topic/feedback-cliente", feedback);
            //Enviar bloco validado para a fila
            rabbitTemplate.convertAndSend("bloco-validado", strBlocoValidado);
            //salva no banco blocovalidado
            blocoValidadoRepositorio.save(blocoValidado);
            System.out.println("                            " + strBlocoValidado);
        } else {
            //rabbitTemplate.convertAndSend("bloco-minerado", strBlocoMinerado);
            System.out.println("");
            System.out.println("[bloco-valida] - enviou bloco de volta....");
            System.out.println("                            " + strBlocoMinerado);
        }
    }
}