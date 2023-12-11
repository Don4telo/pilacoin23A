package br.ufsm.csi.pilacoin23.util;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraPila;
import br.ufsm.csi.pilacoin23.model.Feedback;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class MineraPilas implements InterfaceMineraPila {
    @Autowired
    protected RabbitTemplate template;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private boolean miningRunning = true; // Variável para controlar se a mineração está em execução


    // Este método tenta criar instâncias de PilaCoin para mineração
    @Override
    @SneakyThrows
    public PilaCoinJson criaPila(BigInteger dificuldade) {
        SecureRandom rnd = new SecureRandom();

        System.out.print("[pila-minera] - ");
        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves.leKeyPair().getPublic(), GeraChaves.leKeyPair().getPrivate());
        //?
        //System.out.println("key = " + kp.getPublic().getEncoded());
        System.out.println("Mineracao de Pila$ comecou!");
        //DIFICULDADE = dificuldade;
        //System.out.println("Dificuldade atual " + dificuldade);
        System.out.println("..");

        int tentativas = 0;
        int tentUnit = 0;
        int pilas = 0;
        while (true) {
            tentativas++;
            tentUnit++;

            if (tentativas % 100000 == 0) {
                System.out.print(" .");

            }

            // Constrói um objeto PilaCoin
            PilaCoinJson pilaCoin = PilaCoinJson.builder()
                    .dataCriacao(new Date())
                    .chaveCriador(kp.getPublic().getEncoded())
                    .nonce(new BigInteger(128, rnd).toString())
                    .nomeCriador("alexandre")
                    .build();
            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);
            //System.out.println("pilaJson =" + pilaJson);

            // Gera um hash SHA-256 para o objeto PilaCoin
            MessageDigest md = MessageDigest.getInstance("sha-256");
            byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));
            BigInteger numHash = new BigInteger(hash).abs();
            //System.out.println(numHash);


            // Compara o hash gerado com a dificuldade
            if (numHash.compareTo(dificuldade) < 0) {
                System.out.println("");
                System.out.println("[pila-minera] - Minerou $ $ $ ");

                //System.out.println("numHash = " + numHash);
                System.out.print("[pila-minera] - gerando pila, ");

                //System.out.println("- - pilaCoinJson - - " + pilaJson);
                System.out.println("pila gerado ->");

                System.out.println("[pila-minera] - Tentativas = " + tentativas);
                System.out.println(".....");

                // Construa a mensagem de feedback
                String msg = "[pila-minera] - Minerou uma PilaCoin! Tentativas: " + tentativas;
                Feedback feedback = new Feedback(msg);

                // Enviar feedback para o tópico WebSocket
                messagingTemplate.convertAndSend("/topic/feedback-cliente", feedback);

                // Salva a PilaCoin na base após minerar
                System.out.println("[pila-minera] - inserindo no banco...");
                return pilaCoin;
            }
        }
    }

    // Método para iniciar a mineração
    public void iniciaMineracao() {
        miningRunning = true;
        // Lógica de mineração aqui
        // Certifique-se de verificar miningRunning durante o loop para permitir a interrupção
    }

    // Método para parar a mineração
    public void pararMineracao() {
        miningRunning = false;
    }
}

