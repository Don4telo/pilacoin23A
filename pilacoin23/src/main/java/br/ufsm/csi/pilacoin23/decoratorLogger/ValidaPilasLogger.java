package br.ufsm.csi.pilacoin23.decoratorLogger;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceValidaPilas;
import br.ufsm.csi.pilacoin23.model.PilaValidado;
import br.ufsm.csi.pilacoin23.util.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ValidaPilasLogger implements InterfaceValidaPilas {

    @Autowired
    private final InterfaceValidaPilas interfaceValidaPilas;

    public ValidaPilasLogger(InterfaceValidaPilas validaPilas) {
        this.interfaceValidaPilas = validaPilas;
    }

    @Override
    public  PilaValidado validarPilas(String strPila, BigInteger dificuldade) throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, JsonProcessingException {

        PilaValidado pilaCoin = interfaceValidaPilas.validarPilas(strPila, dificuldade);

        if (pilaCoin != null) {
            // Adiciona funcionalidade de logging
            //System.out.println("[valida-pila-logger] pila validado - " + strPila);
            logMineracao(strPila);

            return pilaCoin;
        }else {
            return null;
        }
    }


    private static void logMineracao(String validado) throws JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = sdf.format(Calendar.getInstance().getTime());
        // Cria um objeto ObjectMapper para desserializar a mensagem JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(validado);


        String logMessage = String.format("[valida-pila] - %s - %s", dataHoraAtual,"pilaValidado nonce =" + node.get("nonce").asText());
        //System.out.println("log message = " + logMessage);

        // Grava o log no arquivo log.txt
        Logger logger = new Logger();
        logger.gravarLogEmArquivo(logMessage);
    }
}
