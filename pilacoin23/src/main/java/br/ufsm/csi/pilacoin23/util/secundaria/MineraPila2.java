package br.ufsm.csi.pilacoin23.util.secundaria;

import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraPila;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class MineraPila2  {
    @Autowired
    protected RabbitTemplate template;


    // Este método tenta criar instâncias de PilaCoin para mineração

    @SneakyThrows
    public PilaCoinJson criaPila(BigInteger dificuldade) {
        SecureRandom rnd = new SecureRandom();

        System.out.print("[pila-minera2] - ");
        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves2.leKeyPair().getPublic(), GeraChaves2.leKeyPair().getPrivate());
        //?
        //System.out.println("key = " + kp.getPublic().getEncoded());
        System.out.println("Mineracao de Pila$$ comecou!");
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
                System.out.print(" -");

            }

            // Constrói um objeto PilaCoin
            PilaCoinJson pilaCoin = PilaCoinJson.builder()
                    .dataCriacao(new Date())
                    .chaveCriador(kp.getPublic().getEncoded())
                    .nonce(new BigInteger(128, rnd).toString())
                    .nomeCriador("ADonato2")
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
                System.out.println("[pila-minera2] - Minerou $ $ $ ");

                //System.out.println("numHash = " + numHash);
                System.out.print("[pila-minera2] - gerando pila, ");

                //System.out.println("- - pilaCoinJson - - " + pilaJson);
                System.out.println("pila gerado ->");

                System.out.println("[pila-minera2] - Tentativas = " + tentativas);
                System.out.println(".....");

                // Salva a PilaCoin na base após minerar
                System.out.println("[pila-minera2] - inserindo no banco...");
                return pilaCoin;
            }
        }
    }

}