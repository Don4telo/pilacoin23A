package br.ufsm.csi.pilacoin23.secundaria;

import br.ufsm.csi.pilacoin23.Persistencia.PilaRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.util.secundaria.MineraPila2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import java.math.BigInteger;



//@Service
public class SecundariaMineracaoPilaServico {

    static BigInteger DIFICULDADE;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PilaRepositorio pilaRepositorio;


    Integer pilas = 0;

    // Este método é um listener para a fila definida em "${queue.dificuldade}"
    // Ele recebe uma mensagem contendo informações de dificuldade para mineração.
    //@RabbitListener(queues = {"${queue.dificuldade}"})
    public void pegaDificuldade(@Payload String strDificuldade) throws JsonProcessingException {
        //System.out.println("Message: " + strDificuldade);

        // Cria um objeto ObjectMapper para desserializar a mensagem JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(strDificuldade);

        // Extrai a dificuldade da mensagem e a converte para um BigInteger
        String stDificuldade = node.get("dificuldade").asText();
        BigInteger dificuldade = new BigInteger(stDificuldade, 16).abs();
        System.out.println("[dificuldade2] - .:Dificuldade da fila    = " + dificuldade + ":.");

        // Extrai outras informações da mensagem, como a data de início
        //String date = node.get("inicio").asText();

        // Exibe as informações obtidas da mensagem
        //System.out.println("stDificuldade: " + stDificuldade);
        //System.out.println("Dificuldade: " + dificuldade);
        //System.out.println("inicio: " + date);
        System.out.println(".");

        // Chama o método criaPila com a dificuldade extraída da mensagem
        MineraPila2 mineraPila = new MineraPila2();
        PilaCoinJson pila = mineraPila.criaPila(dificuldade);
        pilaRepositorio.save(pila);
        String strPilaJson = new ObjectMapper().writeValueAsString(pila);
        rabbitTemplate.convertAndSend("pila-minerado", strPilaJson);
        System.out.println("[pila-minera2] - enviou" + strPilaJson);
        pilas++;
        System.out.println("[pila-minera2] - pila$$$ = " + pilas);
        System.out.println("...");

    }
}