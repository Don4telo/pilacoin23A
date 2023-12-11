package br.ufsm.csi.pilacoin23.util.secundaria;

import br.ufsm.csi.pilacoin23.model.Bloco;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class MineraBloco2 {

    // Este método tenta criar instâncias de Bloco para mineração

    @SneakyThrows
    public Bloco criaBloco(BigInteger dificuldade, Long numeroBloco) {
        SecureRandom rnd = new SecureRandom();
        System.out.print("[bloco-minera2] - ");

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = GeraChaves2.leKeyPair();

        System.out.println("Mineracao de Bloco$ comecou!");
        //System.out.println("Dificuldade atual: " + dificuldade);
        System.out.println("**");

        int tentativas = 0;
        int tentativasUnit = 0;
        int blocosMinerados = 0;

        while (true) {
            tentativas++;
            tentativasUnit++;

            if (tentativas % 100000 == 0) {
                System.out.print(" +");
            }

            // Constrói um objeto Bloco
            Bloco bloco = Bloco.builder()
                    .numeroBloco(numeroBloco)
                    .nonce(new BigInteger(128, rnd).toString())
                    .chaveUsuarioMinerador(kp.getPublic().getEncoded())
                    .nomeUsuarioMinerador("ADonato2")
                    .build();

            String blocoJson = new ObjectMapper().writeValueAsString(bloco);

            // Gera um hash SHA-256 para o objeto Bloco
            MessageDigest md = MessageDigest.getInstance("sha-256");
            byte[] hash = md.digest(blocoJson.getBytes("UTF-8"));
            BigInteger numHash = new BigInteger(hash).abs();

            // Compara o hash gerado com a dificuldade
            if (numHash.compareTo(dificuldade) < 0) {
                System.out.println("");
                System.out.println("[bloco-minera2] - Bloco Minerado # # #");

                System.out.print("[bloco-minera2] - Gerando bloco, ");

                System.out.println("Bloco gerado ->");

                System.out.println("[bloco-minera2] - Tentativas = " + tentativas);
                tentativasUnit = 0;
                blocosMinerados++;
                System.out.println("[bloco-minera2] - Blocos minerados = " + blocosMinerados);
                System.out.println("*****");
                return bloco;
            }
        }
    }
}
