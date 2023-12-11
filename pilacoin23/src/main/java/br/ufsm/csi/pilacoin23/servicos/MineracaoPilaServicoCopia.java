package br.ufsm.csi.pilacoin23.servicos;

import br.ufsm.csi.pilacoin23.Persistencia.PilaRepositorio;
import br.ufsm.csi.pilacoin23.decoratorLogger.MineraPilaLogger;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


//@Service
public class MineracaoPilaServicoCopia {

    private boolean miningRunning = false; // Variável para controlar se a mineração está em execução
    private final Object lock = new Object(); // Objeto para sincronização entre threads

    static BigInteger DIFICULDADE;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PilaRepositorio pilaRepositorio;

    @Autowired
    private MineraPilaLogger mineraPila;

    Integer pilas = 0;

    // Este método é um listener para a fila definida em "${queue.dificuldade}"
    // Ele recebe uma mensagem contendo informações de dificuldade para mineração.
    @RabbitListener(queues = {"${queue.dificuldade}"})
    public void pegaDificuldade(@Payload String strDificuldade) throws JsonProcessingException {
        //System.out.println("Message: " + strDificuldade);

        // Cria um objeto ObjectMapper para desserializar a mensagem JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(strDificuldade);

        // Extrai a dificuldade da mensagem e a converte para um BigInteger
        String stDificuldade = node.get("dificuldade").asText();
        BigInteger dificuldade = new BigInteger(stDificuldade, 16).abs();
        System.out.println("[dificuldade] - .:Dificuldade da fila    = " + dificuldade + ":.");
        System.out.println(".");

        // Chama o método criaPila com a dificuldade extraída da mensagem

        PilaCoinJson pila = mineraPila.criaPila(dificuldade);
        pilaRepositorio.save(pila);
        String strPilaJson = new ObjectMapper().writeValueAsString(pila);
        rabbitTemplate.convertAndSend("pila-minerado", strPilaJson);
        System.out.println("[pila-minera] - enviou" + strPilaJson);
        pilas++;
        System.out.println("[pila-minera] - pila$$$ = " + pilas);
        System.out.println("...");

    }

    // Método para iniciar a mineração
    public void iniciaMineracao() {
        miningRunning = true;
        while (miningRunning) {
            // Lógica de mineração aqui
            // Certifique-se de verificar miningRunning durante o loop para permitir a interrupção
        }
    }

    // Método para parar a mineração
    public void pararMineracao() {
        miningRunning = false;
    }

}