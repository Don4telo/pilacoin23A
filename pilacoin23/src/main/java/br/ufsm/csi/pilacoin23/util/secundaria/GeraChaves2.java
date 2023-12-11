package br.ufsm.csi.pilacoin23.util.secundaria;


import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GeraChaves2 { // Este método lê ou gera um par de chaves pública e privada
    @SneakyThrows
    public static KeyPair leKeyPair() {
        File fpub = new File("oldest_pub.key");
        File fpriv = new File("oldest_priv.key");
        if (fpub.exists() && fpriv.exists()) {
            // Se os arquivos existem, lê os dados das chaves
            FileInputStream pubIn = new FileInputStream(fpub);
            FileInputStream privIn = new FileInputStream(fpriv);
            //System.out.println("encontrou chaves");
            byte[] barrPub = new byte[(int) pubIn.getChannel().size()];
            byte[] barrPriv = new byte[(int) privIn.getChannel().size()];
            pubIn.read(barrPub);
            privIn.read(barrPriv);

            // Converte os dados lidos em objetos de chave pública e privada
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(barrPub));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(barrPriv));

            // Retorna um par de chaves com as chaves pública e privada lidas dos arquivos
            System.out.print("pegando chaves, ");
            return new KeyPair(publicKey, privateKey);
        } else {
            // Caso os arquivos das chaves não existam, gera um novo par de chaves
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();

            // Cria arquivos para armazenar as novas chaves no sistema de arquivos
            FileOutputStream pubOut = new FileOutputStream("oldest_pub.key", false);
            System.out.print("criou chave, ");
            FileOutputStream privOut = new FileOutputStream("oldest_priv.key", false);

            // Escreve os dados da chave pública e privada nos arquivos
            pubOut.write(keyPair.getPublic().getEncoded());
            privOut.write(keyPair.getPrivate().getEncoded());

            // Fecha os arquivos após a escrita
            pubOut.close();
            privOut.close();

            // Retorna o novo par de chaves gerado
            return keyPair;
        }
    }
}

