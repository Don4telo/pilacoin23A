package br.ufsm.csi.pilacoin23.semUso;

import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import br.ufsm.csi.pilacoin23.util.GeraChaves;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;

//@Service
public class ValidacaoBlocos {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static BigInteger DIFICULDADE;

    @SneakyThrows
    //@RabbitListener(queues = {"bloco-minerado"})
    public void validaBloco(@Payload String strBlocoMinerado) {
        System.out.println("");
        System.out.println("[bloco-valida] - Message: BM " + strBlocoMinerado);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger numHash = new BigInteger(md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8))).abs();
        ObjectMapper om = new ObjectMapper();
        //om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Bloco bloco = om.readValue(strBlocoMinerado, Bloco.class);
        byte[] hash = md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8));


        System.out.print("[bloco-valida] - ");
        LeDificuldade leDificuldade = new LeDificuldade();
        DIFICULDADE = leDificuldade.retornaDificuldade();

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves.leKeyPair().getPublic(), GeraChaves.leKeyPair().getPrivate());

        // Condição adicionada: Verifica se o numHash é menor que a dificuldade apenas se o nome do validador for diferente do nome do criador
        if (numHash.compareTo(DIFICULDADE) < 0 && !bloco.getNomeUsuarioMinerador().equals("alexandre")) {
            System.out.println("[bloco-valida] - validando bloco ...");

            //gera assinatura no bloco valido
            Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
            cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            byte[] assinatura = cipherHashAssinatura.doFinal(hash);

            //constrooi bloco validado para envio
            BlocoValidado blocoValidado = BlocoValidado.builder()
                    .nomeValidador("alexandre")
                    .chavePublicaValidador(kp.getPublic().getEncoded())
                    .assinaturaBloco(assinatura)
                    .bloco(bloco)
                    .build();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(blocoValidado);
            System.out.println("[bloco-valida] - bloco validado....");
            rabbitTemplate.convertAndSend("bloco-validado", json);
            System.out.println("                            " + json);

        } else {
            rabbitTemplate.convertAndSend("bloco-minerado", strBlocoMinerado);
            System.out.println("");
            System.out.println("[bloco-valida] - enviou bloco de volta....");
            System.out.println("                            " + strBlocoMinerado);
        }
    }
}
