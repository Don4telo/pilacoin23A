package br.ufsm.csi.pilacoin23.interfaces;

import br.ufsm.csi.pilacoin23.model.PilaValidado;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface InterfaceValidaPilas {
     PilaValidado validarPilas(String strPila, BigInteger dificuldade) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, JsonProcessingException;


}
