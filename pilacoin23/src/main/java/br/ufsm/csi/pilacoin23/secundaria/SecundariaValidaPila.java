package br.ufsm.csi.pilacoin23.secundaria;

import br.ufsm.csi.pilacoin23.Persistencia.PilaValidadoRepositorio;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import br.ufsm.csi.pilacoin23.util.secundaria.GeraChaves2;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;


//@Service
public class SecundariaValidaPila {

    @Autowired
    private PilaValidadoRepositorio pilaValidadoRepositorio;

    private static BigInteger DIFICULDADE;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Método que escuta a fila "pila-minerado"
    //@RabbitListener(queues = {"pila-minerado"})
    public void validatePila(@Payload String strPilaMinerado) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, JsonProcessingException, InvalidKeyException, SignatureException {
        if (!strPilaMinerado.isEmpty()) {
            System.out.println("[pila-valida2] - Message: PM " + strPilaMinerado);

            // Calcular o hash da mensagem
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger numHash = new BigInteger(md.digest(strPilaMinerado.getBytes(StandardCharsets.UTF_8))).abs();
            byte[] hash = md.digest(strPilaMinerado.getBytes(StandardCharsets.UTF_8));

            // Desserializar a string JSON para um objeto PilaCoinJson
            PilaCoinJson pilaCoin = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                pilaCoin = objectMapper.readValue(strPilaMinerado, PilaCoinJson.class);
            } catch (JsonProcessingException e) {
                // Trate a exceção de desserialização JSON
                e.printStackTrace();
            }

            // Obter a dificuldade
            LeDificuldade leDificuldade = new LeDificuldade();
            DIFICULDADE = leDificuldade.retornaDificuldade();

            // Criar um novo objeto PilaCoinJson com base no objeto original, mas sem a assinatura
            PilaCoinJson pila = PilaCoinJson.builder()
                    .dataCriacao(pilaCoin.getDataCriacao())
                    .chaveCriador(pilaCoin.getChaveCriador())
                    .nonce(pilaCoin.getNonce())
                    .nomeCriador(pilaCoin.getNomeCriador())
                    .build();

            System.out.print("[pila-valida2] - ");

            // Gerar um par de chaves RSA
            KeyPair kp = GeraChaves2.leKeyPair();

            // Verificar as condições para validar a pila
            if (DIFICULDADE != null && pilaCoin != null && !pilaCoin.getNomeCriador().equals("ADonato2") && (numHash.compareTo(DIFICULDADE) < 0)) {
                try {
                    // Assinar os dados
                    Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
                    cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
                    byte[] assinatura = cipherHashAssinatura.doFinal(hash);


                    // Construir o objeto PilaValidado
                    PilaValidado pilaValidado = PilaValidado.builder()
                            .nomeValidador("ADonato2")
                            .chavePublicaValidador(kp.getPublic().getEncoded())
                            .assinaturaPilaCoin(assinatura)
                            .pilaCoinJson(pila)
                            .build();

                    System.out.println("[pila-valida2] - Validou $$$");

                    // Serializar o objeto PilaValidado para JSON
                    String json = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(pilaValidado);

                    // Enviar o JSON para a fila "pila-validado"
                    rabbitTemplate.convertAndSend("pila-validado", json);
                    System.out.println("[pila-valida2] - enviou pila valido ....");

                    // Salvar o objeto PilaValidado no repositório
                    pilaValidadoRepositorio.save(pilaValidado);

                    System.out.println("            " + json);
                } catch (Exception e) {
                    // Trate exceções durante o processo de validação
                    e.printStackTrace();
                }
            } else {

                // Se as condições não forem atendidas e nomeCriador não for "luizf", enviar de volta para a fila "pila-minerado"
                if (!"luizf".equals(pilaCoin.getNomeCriador())) {
                    rabbitTemplate.convertAndSend("pila-minerado", strPilaMinerado);
                    System.out.println("enviou pila de volta ....");
                    System.out.println("                            " + strPilaMinerado);
                }

                // Se as condições não forem atendidas, enviar de volta para a fila "pila-minerado"
                //rabbitTemplate.convertAndSend("pila-minerado", strPilaMinerado);
                //System.out.println("enviou pila de volta ....");
                //System.out.println("                            " + strPilaMinerado);
            }
        }
    }
}


/*



@Service
public class ValidaPila {
    @Autowired
    private PilaValidadoRepositorio pilaValidadoRepositorio;

    private static BigInteger DIFICULDADE = null;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {"pila-minerado"})
    public void validatePila(@Payload String strPilaMinerado) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, JsonProcessingException, InvalidKeyException, SignatureException {
        if(!strPilaMinerado.isEmpty()) {
            System.out.println("[pila-valida] - Message: PM " + strPilaMinerado);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger numHash = new BigInteger(md.digest(strPilaMinerado.getBytes(StandardCharsets.UTF_8))).abs();
            PilaCoinJson pilaCoin = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                 pilaCoin = objectMapper.readValue(strPilaMinerado, PilaCoinJson.class);

            } catch(Exception e) {}
            LeDificuldade leDificuldade = new LeDificuldade();
            DIFICULDADE = leDificuldade.retornaDificuldade();

            PilaCoinJson pila = PilaCoinJson.builder()
                    .dataCriacao(pilaCoin.getDataCriacao())
                    .chaveCriador(pilaCoin.getChaveCriador())
                    .nonce(pilaCoin.getNonce())
                    .nomeCriador(pilaCoin.getNomeCriador())
                    .build();

            System.out.print("[pila-valida] - ");
                    // Obtém um par de chaves pública e privada para uso na mineração
            KeyPair kp = new KeyPair(GeraChaves.leKeyPair().getPublic(), GeraChaves.leKeyPair().getPrivate());

            ConversorData conversorData = new ConversorData();
            if (DIFICULDADE != null &&  pilaCoin != null && !pilaCoin.getNomeCriador().equals("ADonato") && (numHash.compareTo(DIFICULDADE) < 0)) {

                byte[] byteHash = numHash.toString().getBytes(StandardCharsets.UTF_8);
                Cipher encryptCipher = Cipher.getInstance("RSA");
                encryptCipher.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
                // Assinar os dados
                byte[] assinatura = encryptCipher.doFinal(byteHash);

                PilaValidado pilaValidado = PilaValidado.builder()
                        .nomeValidador("ADonato")
                        .chavePublicaValidador(kp.getPublic().getEncoded())
                        .assinaturaPilaCoin(assinatura)
                        .pilaCoinJson(pila)
                        .build();
                System.out.println("[pila-valida] - Validou $$$" );
                //ObjectMapper om = new ObjectMapper();
                //om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                //String json = om.writeValueAsString(pilaValidado);
                String json = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(pilaValidado);
                rabbitTemplate.convertAndSend("pila-validado", json);
                System.out.println("[pila-valida] - enviou pila valido ....");
                pilaValidadoRepositorio.save(pilaValidado);
                System.out.println("            " + json);
            } else {
                rabbitTemplate.convertAndSend("pila-minerado", strPilaMinerado);
                System.out.println("enviou pila de volta ....");//[pila-valida] -
                System.out.println("                            " + strPilaMinerado);
            }
        }
    }

}
*/