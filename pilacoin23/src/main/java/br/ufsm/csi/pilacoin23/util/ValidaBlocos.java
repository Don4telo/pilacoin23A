package br.ufsm.csi.pilacoin23.util;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceValidaBlocos;
import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class ValidaBlocos implements InterfaceValidaBlocos {

    @Override
    public BlocoValidado validarBloco(String strBlocoMinerado, BigInteger dificuldade) throws JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger numHash = new BigInteger(md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8))).abs();
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Bloco bloco = om.readValue(strBlocoMinerado, Bloco.class);
        byte[] hash = md.digest(strBlocoMinerado.getBytes(StandardCharsets.UTF_8));

        System.out.print("[bloco-valida] - ");

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves.leKeyPair().getPublic(), GeraChaves.leKeyPair().getPrivate());

        // Condição adicionada: Verifica se o numHash é menor que a dificuldade apenas se o nome do validador for diferente do nome do criador
        if (numHash.compareTo(dificuldade) < 0 && !bloco.getNomeUsuarioMinerador().equals("alexandre")) {
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
            return blocoValidado;
        } else {
            return null;
        }
    }


}
