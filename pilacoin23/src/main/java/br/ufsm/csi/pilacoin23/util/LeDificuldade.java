package br.ufsm.csi.pilacoin23.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class LeDificuldade {

    private static BigInteger DIFICULDADE;

    public BigInteger retornaDificuldade() {
        // Lê a dificuldade do arquivo "dificuldade.txt"
        BigInteger dificuldadeFromFile = lerDificuldadeDoArquivo();
        if (dificuldadeFromFile != null) {
            DIFICULDADE = dificuldadeFromFile;
            //System.out.println("dificuldadeFromFile :: = " + dificuldadeFromFile);
        }
        return dificuldadeFromFile;
    }
    private BigInteger lerDificuldadeDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dificuldade.txt"))) {
            String linha = reader.readLine();
            if (linha != null && !linha.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(linha);
                return new BigInteger(node.get("dificuldade").asText(), 16).abs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

