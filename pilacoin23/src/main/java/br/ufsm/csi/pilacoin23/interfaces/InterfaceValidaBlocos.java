package br.ufsm.csi.pilacoin23.interfaces;

import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface InterfaceValidaBlocos {
    BlocoValidado validarBloco(String strBlocoMinerado, BigInteger dificuldade) throws JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;
}
