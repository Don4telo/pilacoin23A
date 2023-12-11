package br.ufsm.csi.pilacoin23.servicos;


import br.ufsm.csi.pilacoin23.Persistencia.UsuarioRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.model.QueryRequest;
import br.ufsm.csi.pilacoin23.model.QueryResponsePila;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

//@Service
public class DificuldadeServico {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    QueryResponsePila queryResponsePila;

    private static final String DIFICULDADE_FILE_PATH = "dificuldade.txt";

   // @RabbitListener(queues = {"${queue.dificuldade}"})
    public void retornaDificuldade(@Payload String strDificuldade) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(strDificuldade);

        String stDificuldade = node.get("dificuldade").asText();
        BigInteger dificuldadeFromQueue = new BigInteger(stDificuldade, 16).abs();

        // Ler a dificuldade do arquivo
        LeDificuldade leDificuldade = new LeDificuldade();
        BigInteger dificuldadeDoArquivo = leDificuldade.retornaDificuldade();
        //System.out.println(dificuldadeDoArquivo);

        // Comparar as dificuldades
        if (dificuldadeDoArquivo == null || !dificuldadeDoArquivo.equals(dificuldadeFromQueue)) {
            // Se as dificuldades forem diferentes, atualizar o arquivo
            escreverDificuldadeEmArquivo(strDificuldade);
        } else {
            System.out.print(" @");
            //System.out.println(" .:Dificuldade do arquivo = " + dificuldadeDoArquivo + ":.");
        }
    }

    private void escreverDificuldadeEmArquivo(String dificuldade) {
        try (FileWriter writer = new FileWriter(DIFICULDADE_FILE_PATH, false)) {
            writer.write(dificuldade);
            System.out.println();
            System.out.println(" - Dificuldade escrita - ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}