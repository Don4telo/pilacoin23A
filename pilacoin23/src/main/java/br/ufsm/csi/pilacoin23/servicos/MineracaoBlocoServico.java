package br.ufsm.csi.pilacoin23.servicos;


import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.Persistencia.BlocoRepositorio;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import br.ufsm.csi.pilacoin23.decoratorLogger.MineraBlocoLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.math.BigInteger;

@Service
public class MineracaoBlocoServico {

    @Autowired
    private BlocoRepositorio blocoRepositorio;

    static BigInteger DIFICULDADE;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MineraBlocoLogger mineraBloco;

    Integer blocosDescobertos = 0;

   @RabbitListener(queues = {"descobre-bloco"})
    public void descobreBloco(@Payload String strBloco) throws JsonProcessingException {
        System.out.println("[bloco-minera] - Message: BD " + strBloco);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(strBloco);
        Long numeroBloco = node.get("numeroBloco").asLong();
        //String nonceBlocoAnterior = node.get("nonceBlocoAnterior").asText();

        LeDificuldade leDificuldade = new LeDificuldade();
        DIFICULDADE = leDificuldade.retornaDificuldade();
        System.out.println("*");

        // Chama o método criaBloco com a dificuldade extraída da mensagem
        Bloco blocoMinerado = mineraBloco.criaBloco(DIFICULDADE, numeroBloco);
        ObjectMapper objectMapper = new ObjectMapper();
        String stblocoMinerado = objectMapper.writeValueAsString(blocoMinerado);
        blocoRepositorio.save(blocoMinerado);
        // Envie o bloco minerado para a fila
        rabbitTemplate.convertAndSend("bloco-minerado", stblocoMinerado);
        System.out.println("[bloco-minera] - enviou bloco descoberto: " + stblocoMinerado);
        //System.out.println("[bloco-minera] - bloco descoberto: " + strBloco);
        blocosDescobertos++;
        System.out.println("[bloco-minera] - blocos descobertos = " + blocosDescobertos);
        System.out.println("***");
    }
}

