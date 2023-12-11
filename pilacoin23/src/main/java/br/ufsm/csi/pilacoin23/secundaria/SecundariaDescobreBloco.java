package br.ufsm.csi.pilacoin23.secundaria;

import br.ufsm.csi.pilacoin23.Persistencia.BlocoRepositorio;
import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import br.ufsm.csi.pilacoin23.util.secundaria.MineraBloco2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import java.math.BigInteger;

//@Service
public class SecundariaDescobreBloco {

    @Autowired
    private BlocoRepositorio blocoRepositorio;

    static BigInteger DIFICULDADE;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    Integer blocosDescobertos = 0;

   //@RabbitListener(queues = {"descobre-bloco"})
    public void descobreBloco(@Payload String strBloco) throws JsonProcessingException {
        System.out.println("[bloco-minera2] - Message: BD " + strBloco);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(strBloco);
        Long numeroBloco = node.get("numeroBloco").asLong();
        //String nonceBlocoAnterior = node.get("nonceBlocoAnterior").asText();

        LeDificuldade leDificuldade = new LeDificuldade();
        DIFICULDADE = leDificuldade.retornaDificuldade();
        System.out.println("*");

        // Chama o método criaBloco com a dificuldade extraída da mensagem
        MineraBloco2 mineraBloco = new MineraBloco2();
        Bloco blocoMinerado = mineraBloco.criaBloco(DIFICULDADE, numeroBloco);
        ObjectMapper objectMapper = new ObjectMapper();
        String stblocoMinerado = objectMapper.writeValueAsString(blocoMinerado);
        blocoRepositorio.save(blocoMinerado);
        // Envie o bloco minerado para a fila
        rabbitTemplate.convertAndSend("bloco-minerado", stblocoMinerado);
        System.out.println("[bloco-minera2] - enviou bloco descoberto: " + stblocoMinerado);
        //System.out.println("[bloco-minera] - bloco descoberto: " + strBloco);
        blocosDescobertos++;
        System.out.println("[bloco-minera2] - blocos descobertos = " + blocosDescobertos);
        System.out.println("***");
    }
}

