package br.ufsm.csi.pilacoin23.semUso;

import br.ufsm.csi.pilacoin23.Persistencia.PilaValidadoRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import br.ufsm.csi.pilacoin23.util.GeraChaves;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;

//@Service
public class ValidaPila {

    @Autowired
    private PilaValidadoRepositorio pilaValidadoRepositorio;

    private static BigInteger DIFICULDADE = null;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //@RabbitListener(queues = {"pila-minerado"})
    public void validatePila(@Payload String strPilacoin) throws Exception {
        if (!strPilacoin.isEmpty()) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger numHash = new BigInteger(md.digest(strPilacoin.getBytes(StandardCharsets.UTF_8))).abs();

            DIFICULDADE = new LeDificuldade().retornaDificuldade();
            PilaCoinJson pilaCoin = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                pilaCoin = objectMapper.readValue(strPilacoin, PilaCoinJson.class);
            } catch (Exception e) {
                // Lidar com a exceção de parsing, se necessário
            }

            if (DIFICULDADE != null && pilaCoin != null && !pilaCoin.getNomeCriador().equals("alexandre") && (numHash.compareTo(DIFICULDADE) < 0)) {

                byte[] hash = md.digest(strPilacoin.getBytes(StandardCharsets.UTF_8));

                System.out.print("[pila-valida] - ");
                KeyPair kp = GeraChaves.leKeyPair();

                Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
                cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
                byte[] assinatura = cipherHashAssinatura.doFinal(hash);


                PilaValidado pilaValidado = PilaValidado.builder()
                        .nomeValidador("alexandre")
                        .chavePublicaValidador(kp.getPublic().getEncoded())
                        .assinaturaPilaCoin(assinatura)
                        .pilaCoinJson(pilaCoin)
                        .build();

                String json = objectMapper.writeValueAsString(pilaValidado);
                System.out.println("[pila-valida] - Validou $$$");

                // Enviar o JSON para a fila "pila-validado"
                rabbitTemplate.convertAndSend("pila-validado", json);
                System.out.println("[pila-valida] - enviou pila valido ....");
                System.out.println("                            " + json);
            } else {
                System.out.println("");
                System.out.println("[pila-valida] - Nao validou $$$");
                rabbitTemplate.convertAndSend("pila-minerado", strPilacoin);
                System.out.println("[pila-valida] - enviou pila de volta ....");
                System.out.println("                            " + strPilacoin);
            }
        }
    }
}
