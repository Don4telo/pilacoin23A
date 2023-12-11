package br.ufsm.csi.pilacoin23.decoratorLogger;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceValidaBlocos;
import br.ufsm.csi.pilacoin23.model.BlocoValidado;
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
public class ValidaBlocosLogger implements InterfaceValidaBlocos {

    @Autowired
    private final InterfaceValidaBlocos validaBlocos;

    public ValidaBlocosLogger(InterfaceValidaBlocos validaBlocos) {
        this.validaBlocos = validaBlocos;
    }

    @Override
    public BlocoValidado validarBloco(String strBlocoMinerado, BigInteger dificuldade) throws JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        BlocoValidado blocoValidado = validaBlocos.validarBloco(strBlocoMinerado, dificuldade);
        if (blocoValidado != null) {
            // Adiciona funcionalidade de logging
            //System.out.println("[valida-bloco-logger] bloco validado - " + strBlocoMinerado);
            logMineracao(strBlocoMinerado);

            return blocoValidado;
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

        String logMessage = String.format("[valida-bloco] - %s - %s", dataHoraAtual,"blocoValidado nonce =" +  node.get("nonce").asText(),"numeroBloco = " + node.get("numeroBloco").asText());
        //System.out.println(logMessage);

        // Grava o log no arquivo log.txt
        Logger logger = new Logger();
        logger.gravarLogEmArquivo(logMessage);
    }
}