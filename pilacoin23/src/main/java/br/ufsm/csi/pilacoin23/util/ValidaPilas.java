package br.ufsm.csi.pilacoin23.util;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceValidaPilas;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class ValidaPilas implements InterfaceValidaPilas {
    @Override
    public PilaValidado validarPilas(String strPila, BigInteger dificuldade) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Calcula o hash da transação PilaCoin
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger numHash = new BigInteger(md.digest(strPila.getBytes(StandardCharsets.UTF_8))).abs();

        PilaCoinJson pilaCoin = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            // Converte a string JSON em um objeto PilaCoinJson
            pilaCoin = objectMapper.readValue(strPila, PilaCoinJson.class);
        } catch (Exception e) {
            // Lidar com a exceção de parsing, se necessário
        }

        // Verifica se a dificuldade, o objeto PilaCoinJson e o criador são válidos
        if (dificuldade != null && pilaCoin != null && !pilaCoin.getNomeCriador().equals("alexandre") && (numHash.compareTo(dificuldade) < 0)) {

            // Calcula o hash novamente para obter o valor real
            byte[] hash = md.digest(strPila.getBytes(StandardCharsets.UTF_8));
            System.out.print("[pila-valida] - ");

            // Gera um par de chaves para assinatura
            KeyPair kp = GeraChaves.leKeyPair();

            // Inicializa o Cipher para assinatura usando RSA
            Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
            cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            // Calcula a assinatura digital
            byte[] assinatura = cipherHashAssinatura.doFinal(hash);

            // Cria um objeto PilaValidado com as informações validadas
            PilaValidado pilaValidado = PilaValidado.builder()
                    .nomeValidador("alexandre")
                    .chavePublicaValidador(kp.getPublic().getEncoded())
                    .assinaturaPilaCoin(assinatura)
                    .pilaCoinJson(pilaCoin)
                    .build();
            return pilaValidado;

        }else {
            return null;
        }
    }
}
