package br.ufsm.csi.pilacoin23.util;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraBloco;
import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.model.Feedback;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;
@Component
public class MineraBlocos implements InterfaceMineraBloco {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    // Este método tenta criar instâncias de Bloco para mineração

    @Override
    @SneakyThrows
    public Bloco criaBloco(BigInteger dificuldade, Long numeroBloco) {
        SecureRandom rnd = new SecureRandom();
        System.out.print("[bloco-minera] - ");

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = GeraChaves.leKeyPair();

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
                System.out.print(" *");
            }

            // Constrói um objeto Bloco
            Bloco bloco = Bloco.builder()
                    .numeroBloco(numeroBloco)
                    .nonce(new BigInteger(128, rnd).toString())
                    .chaveUsuarioMinerador(kp.getPublic().getEncoded())
                    .nomeUsuarioMinerador("alexandre")
                    .build();

            String blocoJson = new ObjectMapper().writeValueAsString(bloco);

            // Gera um hash SHA-256 para o objeto Bloco
            MessageDigest md = MessageDigest.getInstance("sha-256");
            byte[] hash = md.digest(blocoJson.getBytes("UTF-8"));
            BigInteger numHash = new BigInteger(hash).abs();

            // Compara o hash gerado com a dificuldade
            if (numHash.compareTo(dificuldade) < 0) {
                System.out.println("");
                System.out.println("[bloco-minera] - Bloco Minerado # # #");

                System.out.print("[bloco-minera] - Gerando bloco, ");

                System.out.println("Bloco gerado ->");

                System.out.println("[bloco-minera] - Tentativas = " + tentativas);
                tentativasUnit = 0;
                blocosMinerados++;
                //System.out.println("[bloco-minera] - Blocos minerados = " + blocosMinerados);
                System.out.println("*****");
                //bloco.setNonceBlocoAnterior(nonceBlocoAnterior);

                // Construa a mensagem de feedback
                String msg = "[bloco-minera] - Minerou um Bloco! Tentativas: " + tentativas;
                Feedback feedback = new Feedback(msg);

                // Enviar feedback para o tópico WebSocket
                messagingTemplate.convertAndSend("/topic/feedback-cliente", feedback);

                return bloco;
            }
        }
    }
}
