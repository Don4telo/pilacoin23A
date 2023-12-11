package br.ufsm.csi.pilacoin23.secundaria;

import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import br.ufsm.csi.pilacoin23.util.secundaria.GeraChaves2;
import br.ufsm.csi.pilacoin23.util.LeDificuldade;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;

//@Service
public class SecundariaValidaBloco {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static BigInteger DIFICULDADE;

    @SneakyThrows
    //@RabbitListener(queues = {"bloco-minerado"})
    public void validaBloco(@Payload String strBlocoMinerado) {
        System.out.println("");
        System.out.println("[bloco-valida2] - Message: BM " + strBlocoMinerado);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger numHash = new BigInteger(md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8))).abs();
        ObjectMapper om = new ObjectMapper();
        //om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Bloco bloco = om.readValue(strBlocoMinerado, Bloco.class);
        byte[] hash = md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8));

        // Constrói um objeto Bloco
        Bloco blocoEnvio = Bloco.builder()
                .numeroBloco(bloco.getNumeroBloco())
                .nonceBlocoAnterior(bloco.getNonceBlocoAnterior())
                .nonce(bloco.getNonce())
                .chaveUsuarioMinerador(bloco.getChaveUsuarioMinerador())
                .nomeUsuarioMinerador(bloco.getNomeUsuarioMinerador())
                .transacoes(bloco.getTransacoes())
                .build();

        System.out.print("[bloco-valida2] - ");
        LeDificuldade leDificuldade = new LeDificuldade();
        DIFICULDADE = leDificuldade.retornaDificuldade();

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves2.leKeyPair().getPublic(), GeraChaves2.leKeyPair().getPrivate());

        // Condição adicionada: Verifica se o hash é menor que a dificuldade apenas se o nome do validador for diferente do nome do criador
        if (numHash.compareTo(DIFICULDADE) < 0 && !bloco.getNomeUsuarioMinerador().equals("ADonato2")) {
            System.out.println("[bloco-valida2] - validando bloco ...");

            //gera assinatura no bloco valido
            Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
            cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            byte[] assinatura = cipherHashAssinatura.doFinal(hash);


            // Assina o hash usando a chave privada
            //Signature signature = Signature.getInstance("SHA256withRSA");
            //signature.initSign(kp.getPrivate());
            //signature.update(hashByteArr);
            //byte[] assinaturaBytes = signature.sign();


            BlocoValidado blocoValidado = BlocoValidado.builder()
                    .nomeValidador("ADonato2")
                    .chavePublicaValidador(kp.getPublic().getEncoded())
                    .assinaturaBloco(assinatura)
                    .bloco(bloco)
                    .build();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(blocoValidado);
            System.out.println("[bloco-valida2] - bloco validado....");
            rabbitTemplate.convertAndSend("bloco-validado", json);
            System.out.println(json);

        } else {
            rabbitTemplate.convertAndSend("bloco-minerado", strBlocoMinerado);
            System.out.println("[bloco-valida2] - enviou bloco de volta....");
            System.out.println("            " + strBlocoMinerado);
        }
    }
}
