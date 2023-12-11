package br.ufsm.csi.pilacoin23.servicos;

import br.ufsm.csi.pilacoin23.Persistencia.QueryResponsePilaRepositorio;
import br.ufsm.csi.pilacoin23.model.*;
import br.ufsm.csi.pilacoin23.util.GeraChaves;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class EscutarMihaFila {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    QueryResponsePilaRepositorio queryResponsePilaRepositorio;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "alexandre")
    public void receiveMsgFeedbackUser(@Payload String feedback) throws JsonProcessingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        try {
            System.out.println("[servidor] - ##### Mensagem de feedback recebida: " + feedback);
            // Enviar feedback para o tópico WebSocket
            messagingTemplate.convertAndSend("/topic/feedback", feedback);
        } catch (Exception e) {
            throw new RuntimeException("[servidor] - Erro ao receber mensagem de feedback! ", e);
        }
        Long id = 11L;
        QueryRequest queryJson = QueryRequest.builder()
                .idQuery(Long.valueOf("19"))
                .nomeUsuario("alexandre")
                //.usuario("alexandre")
                .tipoQuery(QueryRequest.TypeQuery.PILA)
                .statusPila(PilaCoinJson.Status.VALIDO)
                .usuarioMinerador("alexandre")
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String queryString = ow.writeValueAsString(queryJson);
        //System.out.println("enviou mensagem = " + queryString);
        //System.out.println("enviou mensagem = " + queryJson);
        //rabbitTemplate.convertAndSend("query",queryString);
        //rabbitTemplate.convertAndSend("query", queryJson);

        // Obtém um par de chaves pública e privada para uso na mineração
        KeyPair kp = new KeyPair(GeraChaves.leKeyPair().getPublic(), GeraChaves.leKeyPair().getPrivate());

        Transacao transacao = Transacao.builder()
                .chaveUsuarioOrigem(kp.getPublic().getEncoded())
                .chaveUsuarioDestino("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0fdDBX76kP+dLOZOe3zCWKvu1la+pCLN96q2OX4hBvMb/rlNdsM//1bVG8664v0b1uow17YffLDTRp5/R55uCARmqrTwoaUKk+Gy6ns0dvhj9X3dEM4px2+FPR0Pdf6HkpkcC8ULX5vNQ4CfIMfTUO991pcZNG5lh6OAdHZ3buD4XIcXKbVMK6l9WZf/4uoi813a9yXlJAzSFkOwdSDJ1V1URz9WnSjJB2VtAQqAteNHwaKHe4Ui2Pl7xRdH/KSWlGfVQCW2H2yZleAY/30e0+meix0otcPFrr3jqV4AunxJI+gaGIPjY2JfYHD5QtUmz7MZBZcdau4efFOecxlL9wIDAQABMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0fdDBX76kP+dLOZOe3zCWKvu1la+pCLN96q2OX4hBvMb/rlNdsM//1bVG8664v0b1uow17YffLDTRp5/R55uCARmqrTwoaUKk+Gy6ns0dvhj9X3dEM4px2+FPR0Pdf6HkpkcC8ULX5vNQ4CfIMfTUO991pcZNG5lh6OAdHZ3buD4XIcXKbVMK6l9WZf/4uoi813a9yXlJAzSFkOwdSDJ1V1URz9WnSjJB2VtAQqAteNHwaKHe4Ui2Pl7xRdH/KSWlGfVQCW2H2yZleAY/30e0+meix0otcPFrr3jqV4AunxJI+gaGIPjY2JfYHD5QtUmz7MZBZcdau4efFOecxlL9wIDAQAB".getBytes())
                .dataTransacao(new Date())
                .nomeUsuarioOrigem("alexandre")
                .nomeUsuarioDestino("ewerton-joaokunde")
                .noncePila("131852717006079078533441087990674669418")
                .build();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String strTransacao = objectMapper.writeValueAsString(transacao);

            // Calcula o hash da transação
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(strTransacao.getBytes(StandardCharsets.UTF_8));

            // Inicializa o Cipher para assinatura usando RSA
            Cipher cipherHashAssinatura = Cipher.getInstance("RSA");
            cipherHashAssinatura.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            // Calcula a assinatura digital
            byte[] assinatura = cipherHashAssinatura.doFinal(hash);



        PilaTransferido pilaTransferido = PilaTransferido.builder()
                .chaveUsuarioOrigem(kp.getPublic().getEncoded())
                .chaveUsuarioDestino("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0fdDBX76kP+dLOZOe3zCWKvu1la+pCLN96q2OX4hBvMb/rlNdsM//1bVG8664v0b1uow17YffLDTRp5/R55uCARmqrTwoaUKk+Gy6ns0dvhj9X3dEM4px2+FPR0Pdf6HkpkcC8ULX5vNQ4CfIMfTUO991pcZNG5lh6OAdHZ3buD4XIcXKbVMK6l9WZf/4uoi813a9yXlJAzSFkOwdSDJ1V1URz9WnSjJB2VtAQqAteNHwaKHe4Ui2Pl7xRdH/KSWlGfVQCW2H2yZleAY/30e0+meix0otcPFrr3jqV4AunxJI+gaGIPjY2JfYHD5QtUmz7MZBZcdau4efFOecxlL9wIDAQABMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0fdDBX76kP+dLOZOe3zCWKvu1la+pCLN96q2OX4hBvMb/rlNdsM//1bVG8664v0b1uow17YffLDTRp5/R55uCARmqrTwoaUKk+Gy6ns0dvhj9X3dEM4px2+FPR0Pdf6HkpkcC8ULX5vNQ4CfIMfTUO991pcZNG5lh6OAdHZ3buD4XIcXKbVMK6l9WZf/4uoi813a9yXlJAzSFkOwdSDJ1V1URz9WnSjJB2VtAQqAteNHwaKHe4Ui2Pl7xRdH/KSWlGfVQCW2H2yZleAY/30e0+meix0otcPFrr3jqV4AunxJI+gaGIPjY2JfYHD5QtUmz7MZBZcdau4efFOecxlL9wIDAQAB".getBytes())
                .nomeUsuarioOrigem("alexandre")
                .nomeUsuarioDestino("ewerton-joaokunde")
                .nonce("131852717006079078533441087990674669418")
                .assinatura(assinatura)
                .dataTransacao(transacao.getDataTransacao())
                .build();

        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String strpilaTransferido = objectMapper.writeValueAsString(transacao);

        rabbitTemplate.convertAndSend("transerir-pila", strpilaTransferido);
        System.out.println("Pila transferido = " + strpilaTransferido);

    }
}

/*
{"msg":"Pila validado com sucesso. São necessárias validacoes de outros usuarios.","nomeUsuario":"alexandre","nonce":"239582774889343045797126516592088450820","queue":"pila-minerado"}

{"msg":"Pila validado por mais da metade dos peers. Mudou status para AG_BLOCO.","nomeUsuario":"alexandre","nonce":"239582774889343045797126516592088450820","queue":"pila-validado"}


[pila-valida] - Message: PM {
  "chaveCriador" : "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0fdDBX76kP+dLOZOe3zCWKvu1la+pCLN96q2OX4hBvMb/rlNdsM//1bVG8664v0b1uow17YffLDTRp5/R55uCARmqrTwoaUKk+Gy6ns0dvhj9X3dEM4px2+FPR0Pdf6HkpkcC8ULX5vNQ4CfIMfTUO991pcZNG5lh6OAdHZ3buD4XIcXKbVMK6l9WZf/4uoi813a9yXlJAzSFkOwdSDJ1V1URz9WnSjJB2VtAQqAteNHwaKHe4Ui2Pl7xRdH/KSWlGfVQCW2H2yZleAY/30e0+meix0otcPFrr3jqV4AunxJI+gaGIPjY2JfYHD5QtUmz7MZBZcdau4efFOecxlL9wIDAQAB",
  "dataCriacao" : 1702239347271,
  "nomeCriador" : "ewerton-joaokunde",
  "nonce" : "8360628640198508970690342192436346583211745384240030970534966384024397230995"
}

TRANSFERIR PILA:
• transferir um pila de sua propriedade, indicando a chave do novo detentor, e
assinando a transação, na queue “traferir-pila”.
• a mensagem terá o formato:
{
“chaveUsuarioOrigem”: “chave púb. do usuário de origem da
transação”,
“chaveUsuarioDestino”: “chave púb. do usuário de destino da
transação”,
“nomeUsuarioOrigem”: “nome usuário origem”,
“nomeUsuarioDestino”: “nome usuário desticno”,
“assinatura”: “assinatura deste objeto pelo usuário de origem”,
“noncePila”: “nonce do pila transferido ou minerado”,
“dataTransacao”: 324234//numero date
“id”: “identificador preenchido pelo servidor”,
“status”: “status preenchido pelo servidor”

}
• a transação somente estará concluída após a sua inserção em um bloco válido,
minerado e validado.

 */